package com.example.Schedule2;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ScheduleService {
    private final Map<Long, ScheduleEntry> scheduleStore = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);
    private final Map<String, String> colorMap = new HashMap<>();
    private final List<String> availableColors = List.of("blue", "green", "yellow", "red", "purple", "indigo");

    public ScheduleEntry addSchedule(ScheduleRequest request) {
        if (request.getStartTime().isAfter(request.getEndTime()) || request.getStartTime().equals(request.getEndTime())) {
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 빨라야 합니다.");
        }
        if (checkConflict(request)) {
            throw new IllegalStateException("시간 충돌이 발생했습니다: " + request.getTitle());
        }

        ScheduleEntry newEntry = new ScheduleEntry(
                request.getTitle(), request.getDayOfWeek(), request.getStartTime(),
                request.getEndTime(), request.getLocation(), request.getMemo()
        );
        newEntry.setId(nextId.getAndIncrement());
        scheduleStore.put(newEntry.getId(), newEntry);
        getColor(newEntry.getTitle());

        return newEntry;
    }

    public ScheduleEntry updateSchedule(Long id, ScheduleRequest request) {
        ScheduleEntry existingEntry = scheduleStore.get(id);
        if (existingEntry == null) {
            throw new NoSuchElementException("ID " + id + "에 해당하는 일정을 찾을 수 없습니다.");
        }
        // **********************************************
        // 수정: 현재 수정하려는 ID를 제외하고 충돌 검사
        // **********************************************
        if (checkConflictForUpdate(id, request)) {
            throw new IllegalStateException("시간 충돌이 발생했습니다: " + request.getTitle());
        }

        existingEntry.setTitle(request.getTitle());
        existingEntry.setDayOfWeek(request.getDayOfWeek());
        existingEntry.setStartTime(request.getStartTime());
        existingEntry.setEndTime(request.getEndTime());
        existingEntry.setLocation(request.getLocation());
        existingEntry.setMemo(request.getMemo());
        getColor(existingEntry.getTitle());

        return existingEntry;
    }

    public Optional<ScheduleEntry> getScheduleById(Long id) {
        return Optional.ofNullable(scheduleStore.get(id));
    }

    public List<ScheduleEntry> getAllSchedules() {
        return new ArrayList<>(scheduleStore.values());
    }

    public void deleteSchedule(Long id) {
        if (scheduleStore.remove(id) == null) {
            throw new NoSuchElementException("ID " + id + "에 해당하는 일정을 찾을 수 없어 삭제할 수 없습니다.");
        }
    }

    /**
     * 새로운 일정 추가 시 충돌 검사
     */
    private boolean checkConflict(ScheduleRequest newRequest) {
        return scheduleStore.values().stream()
                .filter(entry -> entry.getDayOfWeek().equals(newRequest.getDayOfWeek()))
                .anyMatch(entry ->
                        (newRequest.getStartTime().isBefore(entry.getEndTime()) && newRequest.getEndTime().isAfter(entry.getStartTime()))
                );
    }

    /**
     * 기존 일정 업데이트 시 충돌 검사 (자기 자신 제외)
     */
    private boolean checkConflictForUpdate(Long currentId, ScheduleRequest updatedRequest) {
        return scheduleStore.values().stream()
                // 현재 수정하려는 일정이 아닌 다른 일정만 필터링 (핵심!)
                .filter(entry -> !entry.getId().equals(currentId))
                .filter(entry -> entry.getDayOfWeek().equals(updatedRequest.getDayOfWeek()))
                .anyMatch(entry ->
                        (updatedRequest.getStartTime().isBefore(entry.getEndTime()) && updatedRequest.getEndTime().isAfter(entry.getStartTime()))
                );
    }

    public String getColor(String title) {
        return colorMap.computeIfAbsent(title, k -> {
            int index = colorMap.size() % availableColors.size();
            return availableColors.get(index);
        });
    }

    public Map<String, Object> getStats() {
        Map<DayOfWeek, Integer> schedulesByDay = new EnumMap<>(DayOfWeek.class);
        Map<DayOfWeek, Long> minutesByDay = new EnumMap<>(DayOfWeek.class);

        Stream.of(DayOfWeek.values()).forEach(day -> {
            schedulesByDay.put(day, 0);
            minutesByDay.put(day, 0L);
        });

        long totalSchedules = 0;
        long totalMinutes = 0;

        for (ScheduleEntry entry : scheduleStore.values()) {
            DayOfWeek day = entry.getDayOfWeek();
            long duration = ChronoUnit.MINUTES.between(entry.getStartTime(), entry.getEndTime());

            schedulesByDay.merge(day, 1, Integer::sum);
            minutesByDay.merge(day, duration, Long::sum);

            totalSchedules++;
            totalMinutes += duration;
        }

        double averageDurationMinutes = totalSchedules > 0 ? (double) totalMinutes / totalSchedules : 0.0;

        Map<DayOfWeek, Integer> weekdaysSchedules = schedulesByDay.entrySet().stream()
                .filter(e -> e.getKey().getValue() <= 5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<DayOfWeek, Long> weekdaysMinutes = minutesByDay.entrySet().stream()
                .filter(e -> e.getKey().getValue() <= 5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, Object> dataMap = Map.of(
                "totalSchedules", totalSchedules,
                "schedulesByDay", weekdaysSchedules,
                "minutesByDay", weekdaysMinutes,
                "averageDurationMinutes", averageDurationMinutes
        );

        return Map.of("success", true, "data", dataMap);
    }

    public Map<DayOfWeek, Map<String, Object>> generateWeeklyTable() {
        Map<DayOfWeek, Map<String, Object>> weeklyTable = new EnumMap<>(DayOfWeek.class);

        Stream.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)
                .forEach(day -> weeklyTable.put(day, new LinkedHashMap<>()));

        List<LocalTime> timeSlots = new ArrayList<>();
        for (LocalTime t = LocalTime.of(9, 0); t.isBefore(LocalTime.of(18, 0)); t = t.plusMinutes(30)) {
            timeSlots.add(t);
        }

        for (ScheduleEntry entry : scheduleStore.values()) {
            DayOfWeek day = entry.getDayOfWeek();
            Map<String, Object> daySchedule = weeklyTable.get(day);
            if (daySchedule == null) continue;

            long totalMinutes = ChronoUnit.MINUTES.between(entry.getStartTime(), entry.getEndTime());
            int rowspan = (int) (totalMinutes / 30);
            LocalTime entryStart = entry.getStartTime();

            LocalTime startSlot = timeSlots.stream()
                    .filter(slot -> !slot.isAfter(entryStart))
                    .max(LocalTime::compareTo)
                    .orElse(null);

            if (startSlot == null) continue;

            Map<String, Object> blockData = Map.of(
                    "id", entry.getId(),
                    "title", entry.getTitle(),
                    "location", entry.getLocation(),
                    "timeRange", entryStart + " - " + entry.getEndTime(),
                    "rowspan", rowspan,
                    "color", getColor(entry.getTitle())
            );

            daySchedule.put(startSlot.toString(), blockData);

            for (int i = 1; i < rowspan; i++) {
                LocalTime nextSlot = startSlot.plusMinutes(30L * i);
                daySchedule.put(nextSlot.toString(), Map.of("skip", true));
            }
        }

        Map<DayOfWeek, Map<String, Object>> finalWeeklyTable = new EnumMap<>(DayOfWeek.class);
        for(DayOfWeek day : weeklyTable.keySet()) {
            Map<String, Object> sortedDaySchedule = weeklyTable.get(day).entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new
                    ));
            finalWeeklyTable.put(day, sortedDaySchedule);
        }

        return finalWeeklyTable;
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);
    }
}
