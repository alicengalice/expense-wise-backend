package com.expensewise.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;

/**
 * Request DTO for updating an existing Expense.
 * All fields are optional - only provided fields will be updated.
 * This allows partial updates (PATCH-like behavior).
 */
public record ExpenseUpdateDTO( 
    String description,
    
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    BigDecimal amount,
    
    LocalDate date,
    Long categoryId,
    Long userId
) {}
