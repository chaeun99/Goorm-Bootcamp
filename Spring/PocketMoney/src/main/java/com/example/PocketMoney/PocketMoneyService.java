package com.example.PocketMoney;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class PocketMoneyService {
    private final ConcurrentHashMap<Long, PocketMoneyEntry> entries = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @PostConstruct
    public void init() {
        addEntry("학식 점심", new BigDecimal("4500"), "식비", LocalDate.now(), "친구와 함께");
        addEntry("아메리카노", new BigDecimal("3000"), "간식", LocalDate.now(), "공부하면서");
        addEntry("버스비", new BigDecimal("1400"), "교통비", LocalDate.now(), "학교 왕복");
        addEntry("온라인 쇼핑", new BigDecimal("6600"), "쇼핑", LocalDate.now().minusDays(1), "티셔츠 구매");
        addEntry("영화 관람", new BigDecimal("20000"), "문화생활", LocalDate.now().minusMonths(1).withDayOfMonth(15), "지난 달 지출");
    }

    // 새 지출 내역 추가(addEntry)
    public String addEntry(String description, BigDecimal amount, String category, LocalDate date, String memo) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return "올바른 금액을 입력해주세요.";
        }
        if (description == null || description.trim().isEmpty()) {
            return "내용을 입력해주세요.";
        }

        PocketMoneyEntry newEntry = new PocketMoneyEntry(
                idGenerator.incrementAndGet(),
                description,
                amount,
                category,
                date,
                memo
        );
        entries.put(newEntry.getId(), newEntry);
        return null;
    }

    // 모든 내역 최신순 반환(finaAll)
    public List<PocketMoneyEntry> findAll() {
        return entries.values().stream()
                .sorted(Comparator.comparing(PocketMoneyEntry::getDate).reversed()
                        .thenComparing(PocketMoneyEntry::getId).reversed())
                .collect(Collectors.toList());
    }

    // 월 내역 반환(findByMonth)
    public List<PocketMoneyEntry> findByMonth(YearMonth month) {
        return findAll().stream()
                .filter(e -> YearMonth.from(e.getDate()).equals(month))
                .collect(Collectors.toList());
    }

    // 내역 삭제(deleteById)
    public boolean deleteById(Long id) {
        return entries.remove(id) != null;
    }

    // 월 총 지출액(getMonthTotal)
    public BigDecimal getMonthTotal(YearMonth month) {
        return findByMonth(month).stream()
                .map(PocketMoneyEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 월 카테고리별 지출액(getCategoryTotals)
    public Map<String, BigDecimal> getCategoryTotals(YearMonth month) {
        return findByMonth(month).stream()
                .collect(Collectors.groupingBy(
                        PocketMoneyEntry::getCategory,
                        Collectors.mapping(PocketMoneyEntry::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }

    // 월 일일 평균 지출액(getDailyAverage)
    public BigDecimal getDailyAverage(YearMonth month) {
        List<PocketMoneyEntry> monthEntries = findByMonth(month);
        if (monthEntries.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = getMonthTotal(month);

        long daysCounted = monthEntries.stream()
                .map(PocketMoneyEntry::getDate)
                .distinct()
                .count();

        if (daysCounted == 0) return BigDecimal.ZERO;

        return total.divide(new BigDecimal(daysCounted), 0, RoundingMode.HALF_UP);
    }

    // 월 최대 지출 카테고리(getTopSpendingCategory)
    public String getTopSpendingCategory(YearMonth month) {
        Map<String, BigDecimal> totals = getCategoryTotals(month);

        return totals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("없음");
    }
}