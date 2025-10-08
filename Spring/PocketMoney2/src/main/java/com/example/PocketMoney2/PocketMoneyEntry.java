package com.example.PocketMoney2;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PocketMoneyEntry {
    private Long id;
    private String description;
    private BigDecimal amount;
    private String category;
    private LocalDate date;
    private String memo;

    public PocketMoneyEntry(Long id, String description, BigDecimal amount, String category, LocalDate date, String memo) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.memo = memo;
    }

    public PocketMoneyEntry() {
    }

    public Long getId() { return id; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }
    public String getMemo() { return memo; }

    public void setId(Long id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setMemo(String memo) { this.memo = memo; }
}