package com.expensewise.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseSummaryDTO(
    LocalDate date,
    String period,
    BigDecimal totalAmount,
    Integer expenseCount,
    LocalDate periodStartDate,
    LocalDate periodEndDate
) {}
