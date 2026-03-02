package com.expensewise.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating a new Expense.
 * Contains only the fields needed for creation (no ID).
 * Uses IDs for relationships instead of full nested objects.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseCreateDTO {
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotNull(message = "Date is required")
    private LocalDate date;
    
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
}
