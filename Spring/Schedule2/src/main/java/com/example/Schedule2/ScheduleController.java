package com.example.Schedule2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, Object>> handleConflictAndValidation(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", ex.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", ex.getMessage()));
    }

    // 1. 전체 조회 (GET /api/schedules)
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllSchedules() {
        List<ScheduleEntry> schedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(Map.of("success", true, "data", schedules));
    }

    // 2. 단건 조회 (GET /api/schedules/{id})
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleEntry> getScheduleById(@PathVariable Long id) {
        return scheduleService.getScheduleById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoSuchElementException("ID " + id + "에 해당하는 일정을 찾을 수 없습니다."));
    }

    // 3. 생성 (POST /api/schedules)
    @PostMapping
    public ResponseEntity<Map<String, Object>> createSchedule(@RequestBody ScheduleRequest request) {
        ScheduleEntry createdEntry = scheduleService.addSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", true, "data", createdEntry));
    }

    // 4. 수정 (PUT /api/schedules/{id})
    // @PutMapping 대신 @RequestMapping(method = RequestMethod.PUT)을 사용하여 명시적으로 PUT 매핑
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequest request) {
        ScheduleEntry updatedEntry = scheduleService.updateSchedule(id, request);
        return ResponseEntity.ok(Map.of("success", true, "data", updatedEntry));
    }

    // 5. 삭제 (DELETE /api/schedules/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok(Map.of("success", true, "message", "삭제 성공"));
    }

    // 6. 주간 시간표 조회 (GET /api/schedules/weekly-table)
    @GetMapping("/weekly-table")
    public ResponseEntity<Map<String, Object>> getWeeklyTable() {
        Map<DayOfWeek, Map<String, Object>> weeklyTable = scheduleService.generateWeeklyTable();
        return ResponseEntity.ok(Map.of("success", true, "data", weeklyTable));
    }

    // 7. 통계 조회 (GET /api/schedules/stats)
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(scheduleService.getStats());
    }

    // 기타 엔드포인트
    @GetMapping("/colors/{title}")
    public ResponseEntity<Map<String, String>> getScheduleColor(@PathVariable String title) {
        String color = scheduleService.getColor(title);
        return ResponseEntity.ok(Map.of("title", title, "color", color));
    }

    @GetMapping("/days-of-week")
    public ResponseEntity<Map<String, Object>> getDaysOfWeek() {
        return ResponseEntity.ok(Map.of("success", true, "data", scheduleService.getDaysOfWeek()));
    }
}
