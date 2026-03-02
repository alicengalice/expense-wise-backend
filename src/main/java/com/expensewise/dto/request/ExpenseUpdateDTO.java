package com.expensewise.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating an existing Expense.
 * All fields are optional - only provided fields will be updated.
 * This allows partial updates (PATCH-like behavior).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseUpdateDTO {
    
    private String description;
    
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    private LocalDate date;
    
    private Long categoryId;
    
    private Long userId;
}
