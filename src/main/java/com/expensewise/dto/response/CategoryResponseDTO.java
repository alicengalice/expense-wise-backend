package com.expensewise.dto.response;

/**
 * Response DTO for Category entity.
 * Contains only the data that should be exposed to the API.
 */
public record CategoryResponseDTO(
    Long id,
    String name,
    String description
) {}
