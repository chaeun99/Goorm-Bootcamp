package com.example.PocketMoney2;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class PocketMoneyService {
    private final ConcurrentHashMap<Long, PocketMoneyEntry> entries = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    private static final List<String> ALL_CATEGORIES = List.of(
            "식비", "교통비", "간식", "쇼핑", "문화생활", "기타"
    );

    @PostConstruct
    public void init() {
        addEntry(new ExpenseRequest("학식 점심", new BigDecimal("4500"), "식비", LocalDate.now(), "친구와 함께"));
        addEntry(new ExpenseRequest("아메리카노", new BigDecimal("3000"), "간식", LocalDate.now(), "공부하면서"));
        addEntry(new ExpenseRequest("버스비", new BigDecimal("1400"), "교통비", LocalDate.now(), "학교 왕복"));
        addEntry(new ExpenseRequest("온라인 쇼핑", new BigDecimal("6600"), "쇼핑", LocalDate.now().minusDays(1), "티셔츠 구매"));
        addEntry(new ExpenseRequest("영화 관람", new BigDecimal("20000"), "문화생활", LocalDate.now().minusMonths(1).withDayOfMonth(15), "지난 달 지출"));
    }

    public PocketMoneyEntry addEntry(ExpenseRequest request) {
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("올바른 금액을 입력해주세요.");
        }
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
        if (request.getDate() == null) {
            request.setDate(LocalDate.now());
        }

        PocketMoneyEntry newEntry = new PocketMoneyEntry(
                idGenerator.incrementAndGet(),
                request.getDescription(),
                request.getAmount(),
                request.getCategory(),
                request.getDate(),
                request.getMemo()
        );
        entries.put(newEntry.getId(), newEntry);
        return newEntry;
    }

    public List<PocketMoneyEntry> findAll() {
        return entries.values().stream()
                .sorted(Comparator.comparing(PocketMoneyEntry::getDate).reversed()
                        .thenComparing(PocketMoneyEntry::getId).reversed())
                .collect(Collectors.toList());
    }

    public Optional<PocketMoneyEntry> findById(Long id) {
        return Optional.ofNullable(entries.get(id));
    }

    public List<PocketMoneyEntry> findByMonth(YearMonth month) {
        return findAll().stream()
                .filter(e -> YearMonth.from(e.getDate()).equals(month))
                .collect(Collectors.toList());
    }

    public PocketMoneyEntry updateEntry(Long id, ExpenseRequest request) {
        PocketMoneyEntry existingEntry = findById(id)
                .orElseThrow(() -> new NoSuchElementException("수정하려는 내역(ID: " + id + ")을 찾을 수 없습니다."));

        existingEntry.setDescription(request.getDescription());
        existingEntry.setAmount(request.getAmount());
        existingEntry.setCategory(request.getCategory());
        existingEntry.setDate(request.getDate());
        existingEntry.setMemo(request.getMemo());

        return existingEntry;
    }

    public boolean deleteById(Long id) {
        return entries.remove(id) != null;
    }

    public BigDecimal getMonthTotal(YearMonth month) {
        return findByMonth(month).stream()
                .map(PocketMoneyEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<String, BigDecimal> getCategoryTotals(YearMonth month) {
        Map<String, BigDecimal> totals = findByMonth(month).stream()
                .collect(Collectors.groupingBy(
                        PocketMoneyEntry::getCategory,
                        Collectors.mapping(PocketMoneyEntry::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        ALL_CATEGORIES.forEach(category -> totals.putIfAbsent(category, BigDecimal.ZERO));

        return new TreeMap<>(totals);
    }

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

    public String getTopSpendingCategory(YearMonth month) {
        Map<String, BigDecimal> totals = getCategoryTotals(month);

        return totals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("없음");
    }

    public List<String> getAllCategories() {
        return ALL_CATEGORIES;
    }
}