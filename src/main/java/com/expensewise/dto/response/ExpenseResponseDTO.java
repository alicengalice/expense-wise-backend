package com.expensewise.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for Expense entity.
 * Contains nested DTOs for relationships to avoid lazy loading issues
 * and provide a predictable API contract.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponseDTO {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    
    // Nested DTOs instead of full entities - no lazy loading issues!
    private CategoryResponseDTO category;
    private UserResponseDTO user;
}
