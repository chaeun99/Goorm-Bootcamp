package com.example.Schedule;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ScheduleEntry {
    private Long id;
    private String title;
    private String dayOfWeek; // 예: "MON", "TUE", ...
    private String startTime; // 예: "09:00"
    private String endTime;   // 예: "10:30"
    private String location;
    private String memo;

    private static final Map<String, String> KOREAN_DAYS = new HashMap<>();
    static {
        KOREAN_DAYS.put("MON", "월요일");
        KOREAN_DAYS.put("TUE", "화요일");
        KOREAN_DAYS.put("WED", "수요일");
        KOREAN_DAYS.put("THU", "목요일");
        KOREAN_DAYS.put("FRI", "금요일");
    }

    public ScheduleEntry() {}

    public ScheduleEntry(String title, String dayOfWeek, String startTime, String endTime, String location, String memo) {
        this.title = title;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.memo = memo;
    }

    public String getKoreanDay() {
        return KOREAN_DAYS.getOrDefault(this.dayOfWeek, this.dayOfWeek);
    }

    public String getTimeRange() {
        return this.startTime + "~" + this.endTime;
    }

    public boolean checkTimeSlot(String targetDay, String targetTime) {
        if (!this.dayOfWeek.equals(targetDay)) {
            return false;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime start = LocalTime.parse(this.startTime, formatter);
            LocalTime end = LocalTime.parse(this.endTime, formatter);
            LocalTime target = LocalTime.parse(targetTime, formatter);

            return (target.equals(start) || target.isAfter(start)) && target.isBefore(end);

        } catch (Exception e) {
            System.err.println("시간 형식 오류 발생: " + e.getMessage());
            return false;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return getKoreanDay() + " " + getTimeRange() + " " + title + " (" + location + ")";
    }
}
