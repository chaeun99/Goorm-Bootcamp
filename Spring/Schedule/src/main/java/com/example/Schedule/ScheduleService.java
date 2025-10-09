package com.example.Schedule;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private final Map<Long, ScheduleEntry> scheduleStore = new HashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);
    private static final List<String> DAYS_OF_WEEK = List.of("MON", "TUE", "WED", "THU", "FRI");
    private static final List<String> TIME_SLOTS;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static class ScheduleBlock {
        public ScheduleEntry entry;
        public int rowspan;
        public String color;

        public ScheduleBlock(ScheduleEntry entry, int rowspan, String color) {
            this.entry = entry;
            this.rowspan = rowspan;
            this.color = color;
        }
    }

    static {
        TIME_SLOTS = new ArrayList<>();
        LocalTime time = LocalTime.of(9, 0);
        LocalTime endLimit = LocalTime.of(20, 0);
        while (time.isBefore(endLimit)) {
            TIME_SLOTS.add(time.format(TIME_FORMATTER));
            time = time.plusMinutes(30);
        }
    }

    public boolean addSchedule(ScheduleEntry newEntry) {
        if (checkConflict(newEntry)) {
            return false;
        }

        newEntry.setId(nextId.getAndIncrement());
        newEntry.setStartTime(LocalTime.parse(newEntry.getStartTime()).format(TIME_FORMATTER));
        newEntry.setEndTime(LocalTime.parse(newEntry.getEndTime()).format(TIME_FORMATTER));
        scheduleStore.put(newEntry.getId(), newEntry);
        return true;
    }

    public void deleteSchedule(Long id) {
        scheduleStore.remove(id);
    }

    public List<ScheduleEntry> findAll() {
        return scheduleStore.values().stream()
                .sorted(Comparator
                        .comparing((ScheduleEntry e) -> DAYS_OF_WEEK.indexOf(e.getDayOfWeek()))
                        .thenComparing(e -> LocalTime.parse(e.getStartTime(), TIME_FORMATTER)))
                .collect(Collectors.toList());
    }

    public boolean checkConflict(ScheduleEntry newEntry) {
        try {
            LocalTime newStart = LocalTime.parse(newEntry.getStartTime(), TIME_FORMATTER);
            LocalTime newEnd = LocalTime.parse(newEntry.getEndTime(), TIME_FORMATTER);
            String newDay = newEntry.getDayOfWeek();
            if (!newEnd.isAfter(newStart)) {
                return true;
            }
            for (ScheduleEntry existingEntry : scheduleStore.values()) {
                if (!existingEntry.getDayOfWeek().equals(newDay)) {
                    continue;
                }

                LocalTime existingStart = LocalTime.parse(existingEntry.getStartTime(), TIME_FORMATTER);
                LocalTime existingEnd = LocalTime.parse(existingEntry.getEndTime(), TIME_FORMATTER);

                if (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("시간 파싱 중 오류 발생: " + e.getMessage());
            return true;
        }
    }

    public String getColor(String title) {
        if (title == null || title.isEmpty()) {
            return "bg-gray-200";
        }

        int hash = Math.abs(title.hashCode());
        int index = hash % 5;

        return switch (index) {
            case 0 -> "bg-blue-200 text-blue-800 border-blue-400";
            case 1 -> "bg-green-200 text-green-800 border-green-400";
            case 2 -> "bg-yellow-200 text-yellow-800 border-yellow-400";
            case 3 -> "bg-purple-200 text-purple-800 border-purple-400";
            case 4 -> "bg-pink-200 text-pink-800 border-pink-400";
            default -> "bg-gray-200 text-gray-800 border-gray-400";
        };
    }

    public Map<String, Map<String, ScheduleBlock>> generateWeeklyTable(List<ScheduleEntry> allSchedules) {
        Map<String, Map<String, ScheduleBlock>> weeklyTable = new LinkedHashMap<>();
        for (String day : DAYS_OF_WEEK) {
            weeklyTable.put(day, new LinkedHashMap<>());
            for (String timeSlot : TIME_SLOTS) {
                weeklyTable.get(day).put(timeSlot, null);
            }
        }
        for (ScheduleEntry entry : allSchedules) {
            String day = entry.getDayOfWeek();
            LocalTime start = LocalTime.parse(entry.getStartTime(), TIME_FORMATTER);
            LocalTime end = LocalTime.parse(entry.getEndTime(), TIME_FORMATTER);
            if (!DAYS_OF_WEEK.contains(day)) continue;

            long durationMinutes = ChronoUnit.MINUTES.between(start, end);
            int rowspan = (int) (durationMinutes / 30);

            if (rowspan <= 0) continue;

            String startSlot = start.format(TIME_FORMATTER);
            if (weeklyTable.get(day).containsKey(startSlot) && weeklyTable.get(day).get(startSlot) == null) {
                String color = getColor(entry.getTitle());
                ScheduleBlock block = new ScheduleBlock(entry, rowspan, color);
                weeklyTable.get(day).put(startSlot, block);

                LocalTime current = start.plusMinutes(30);
                for (int i = 1; i < rowspan; i++) {
                    String currentSlot = current.format(TIME_FORMATTER);
                    if (weeklyTable.get(day).containsKey(currentSlot)) {

                        weeklyTable.get(day).put(currentSlot, new ScheduleBlock(null, 0, null));
                    }
                    current = current.plusMinutes(30);
                }
            }
        }

        return weeklyTable;
    }

    public List<String> getDaysOfWeek() {
        return DAYS_OF_WEEK;
    }

    public List<String> getTimeSlots() {
        return TIME_SLOTS;
    }
}
