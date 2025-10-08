package com.example.PocketMoney2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expenses")
public class PocketMoneyController {
    private final PocketMoneyService pocketMoneyService;

    public PocketMoneyController(PocketMoneyService pocketMoneyService) {
        this.pocketMoneyService = pocketMoneyService;
    }

    @GetMapping
    public ResponseEntity<List<PocketMoneyEntry>> findAllEntries() {
        return ResponseEntity.ok(pocketMoneyService.findAll());
    }

    @GetMapping(params = {"year", "month"})
    public ResponseEntity<List<PocketMoneyEntry>> findEntriesByMonth(
            @RequestParam int year,
            @RequestParam int month) {

        YearMonth yearMonth = YearMonth.of(year, month);
        return ResponseEntity.ok(pocketMoneyService.findByMonth(yearMonth));
    }

    @PostMapping
    public ResponseEntity<Object> addEntry(@RequestBody ExpenseRequest request) {
        try {
            PocketMoneyEntry newEntry = pocketMoneyService.addEntry(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(newEntry);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEntry(@PathVariable Long id, @RequestBody ExpenseRequest request) {
        try {
            PocketMoneyEntry updatedEntry = pocketMoneyService.updateEntry(id, request);
            return ResponseEntity.ok(updatedEntry);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "수정 요청 형식이 올바르지 않습니다."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEntry(@PathVariable Long id) {
        boolean deleted = pocketMoneyService.deleteById(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "삭제하려는 내역을 찾을 수 없습니다."));
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(pocketMoneyService.getAllCategories());
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getMonthlyStats(
            @RequestParam (required = false) Integer year,
            @RequestParam (required = false) Integer month) {

        YearMonth yearMonth;
        if (year != null && month != null) {
            yearMonth = YearMonth.of(year, month);
        } else {
            yearMonth = YearMonth.now();
        }
        List<PocketMoneyEntry> monthEntries = pocketMoneyService.findByMonth(yearMonth);
        BigDecimal monthTotal = pocketMoneyService.getMonthTotal(yearMonth);
        BigDecimal dailyAverage = pocketMoneyService.getDailyAverage(yearMonth);
        Map<String, BigDecimal> categoryTotals = pocketMoneyService.getCategoryTotals(yearMonth);
        String topCategory = pocketMoneyService.getTopSpendingCategory(yearMonth);

        Map<String, Object> statsData = Map.of(
                "month", yearMonth.toString(),
                "totalAmount", monthTotal.intValue(),
                "dailyAverage", dailyAverage.intValue(),
                "categoryTotals", categoryTotals.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,e -> e.getValue().intValue())),
                "topCategory", topCategory,
                "entryCount", monthEntries.size()
        );

        return ResponseEntity.ok(Map.of("success", true, "data", statsData));
    }
}
