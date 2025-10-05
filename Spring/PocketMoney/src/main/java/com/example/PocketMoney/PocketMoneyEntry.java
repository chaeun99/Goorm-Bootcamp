package com.example.PocketMoney;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PocketMoneyEntry {
    private static final DecimalFormat AMOUNT_FORMATTER = new DecimalFormat("###,###원");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

    private final Long id;
    private final String description;
    private final BigDecimal amount;
    private final String category;
    private final LocalDate date;
    private final String memo;

    public PocketMoneyEntry(Long id, String description, BigDecimal amount, String category, LocalDate date, String memo) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.memo = memo;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getMemo() {
        return memo;
    }

    public String getFormattedAmount() {
        return AMOUNT_FORMATTER.format(amount);
    }

    public String getFormattedDate() {
        return date.format(DATE_FORMATTER);
    }
}
