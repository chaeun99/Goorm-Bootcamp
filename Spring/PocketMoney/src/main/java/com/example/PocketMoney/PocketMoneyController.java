package com.example.PocketMoney;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// http://localhost:8080/

@Controller
public class PocketMoneyController {
    private final PocketMoneyService pocketMoneyService;

    @Autowired
    public PocketMoneyController(PocketMoneyService pocketMoneyService) {
        this.pocketMoneyService = pocketMoneyService;
    }

    // 월별 목록 및 통계 표시
    @GetMapping("/")
    public String listEntries(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            Model model) {

        YearMonth currentMonth;
        if (year != null && month != null) {
            currentMonth = YearMonth.of(year, month);
        } else {
            currentMonth = YearMonth.now();
        }

        List<PocketMoneyEntry> entries = pocketMoneyService.findByMonth(currentMonth);
        BigDecimal monthTotal = pocketMoneyService.getMonthTotal(currentMonth);
        BigDecimal dailyAverage = pocketMoneyService.getDailyAverage(currentMonth);
        Map<String, BigDecimal> categoryTotals = pocketMoneyService.getCategoryTotals(currentMonth);
        String topCategory = pocketMoneyService.getTopSpendingCategory(currentMonth);

        Map<String, String> categoryPercentages;
        if (monthTotal.compareTo(BigDecimal.ZERO) > 0) {
            categoryPercentages = categoryTotals.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().multiply(new BigDecimal("100"))
                                    .divide(monthTotal, 0, RoundingMode.HALF_UP)
                                    .toString() + "%"
                    ));
        } else {
            categoryPercentages = categoryTotals.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> "0%"
                    ));
        }

        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("entries", entries);
        model.addAttribute("monthTotal", monthTotal);
        model.addAttribute("dailyAverage", dailyAverage);
        model.addAttribute("topCategory", topCategory);
        model.addAttribute("categoryTotals", categoryTotals);
        model.addAttribute("categoryPercentages", categoryPercentages);

        model.addAttribute("prevMonth", currentMonth.minusMonths(1));
        model.addAttribute("nextMonth", currentMonth.plusMonths(1));

        return "pocket-money";
    }

    // add 경로에서 지출 추가
    @PostMapping("/add")
    public String addEntry(
            @RequestParam String description,
            @RequestParam String amount,
            @RequestParam String category,
            @RequestParam String date,
            @RequestParam String memo,
            RedirectAttributes redirectAttributes) {

        try {
            BigDecimal amountDecimal = new BigDecimal(amount);
            LocalDate entryDate = LocalDate.parse(date);

            String errorMessage = pocketMoneyService.addEntry(
                    description,
                    amountDecimal,
                    category,
                    entryDate,
                    memo
            );

            if (errorMessage != null) {
                redirectAttributes.addFlashAttribute("errorMessage", "❌ " + errorMessage);
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "✅ 용돈 사용 내역이 성공적으로 추가되었습니다!");
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "❌ 금액 또는 날짜 형식이 올바르지 않습니다.");
        }

        return "redirect:/";
    }

    // /delete/{id} 경로에서 삭제 처리
    @GetMapping("/delete/{id}")
    public String deleteEntry(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = pocketMoneyService.deleteById(id);

        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "✅ 내역이 성공적으로 삭제되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "❌ 삭제하려는 내역을 찾을 수 없습니다.");
        }

        return "redirect:/";
    }
}