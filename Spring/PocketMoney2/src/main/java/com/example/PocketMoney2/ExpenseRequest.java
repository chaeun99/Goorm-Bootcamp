package com.example.PocketMoney2;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseRequest {
    private String description;
    private BigDecimal amount;
    private String category;
    private LocalDate date;
    private String memo;

    public ExpenseRequest(String description, BigDecimal amount, String category, LocalDate date, String memo) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.memo = memo;
    }

    public ExpenseRequest() {
    }

    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }
    public String getMemo() { return memo; }

    public void setDescription(String description) { this.description = description; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setMemo(String memo) { this.memo = memo; }
}