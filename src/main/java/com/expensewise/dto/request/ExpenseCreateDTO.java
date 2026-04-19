package com.expensewise.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for creating a new Expense.
 * Contains only the fields needed for creation (no ID).
 * Uses IDs for relationships instead of full nested objects.
 */
public record ExpenseCreateDTO(
    @NotBlank(message = "Description is required")
    String description,
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    BigDecimal amount,
    
    @NotNull(message = "Date is required")
    LocalDate date,
    
    @NotNull(message = "Category ID is required")
    Long categoryId,
    
    @NotNull(message = "User ID is required")
    Long userId
) {}
