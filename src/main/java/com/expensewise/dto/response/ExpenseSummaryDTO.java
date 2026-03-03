package com.expensewise.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseSummaryDTO {
    private LocalDate date;
    private String period; // "daily", "weekly", "monthly"
    private BigDecimal totalAmount;
    private Integer expenseCount;
    private LocalDate periodStartDate;
    private LocalDate periodEndDate;
    
    public ExpenseSummaryDTO(LocalDate date, String period, BigDecimal totalAmount, Integer expenseCount,
            LocalDate periodStartDate, LocalDate periodEndDate) {
        this.date = date;
        this.period = period;
        this.totalAmount = totalAmount;
        this.expenseCount = expenseCount;
        this.periodStartDate = periodStartDate;
        this.periodEndDate = periodEndDate;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getPeriod() {
        return period;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Integer getExpenseCount() {
        return expenseCount;
    }

    public LocalDate getPeriodStartDate() {
        return periodStartDate;
    }

    public LocalDate getPeriodEndDate() {
        return periodEndDate;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setExpenseCount(Integer expenseCount) {
        this.expenseCount = expenseCount;
    }

    public void setPeriodStartDate(LocalDate periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public void setPeriodEndDate(LocalDate periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

}
