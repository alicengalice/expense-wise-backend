package com.expensewise.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for User entity.
 * Excludes sensitive data and relationships to prevent circular references.
 */
public record UserResponseDTO(
    Long id,
    String username,
    String email,
    LocalDateTime createdAt
) {}
