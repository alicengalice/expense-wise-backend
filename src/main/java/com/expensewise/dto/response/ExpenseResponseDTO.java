package com.expensewise.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Response DTO for Expense entity.
 * Contains nested DTOs for relationships to avoid lazy loading issues
 * and provide a predictable API contract.
 */
public record ExpenseResponseDTO(
    Long id,
    String description,
    BigDecimal amount,
    LocalDate date,
    CategoryResponseDTO category,
    UserResponseDTO user
) {}
