package com.expensewise.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;

public class PaginationUtil {
    
    private PaginationUtil() {
        // Utility class - prevent instantiation
    }

    public static Pageable createPageable(int start, int end, String sort, String order) {
        if (start < 0 || end < 0 || start > end) {
            throw new IllegalArgumentException("Invalid pagination parameters");
        }

        int pageSize = end - start;
        int pageNumber = start / pageSize;
        
        Sort.Direction direction = "ASC".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        
        // Default to sorting by 'id' if no sort field is provided
        String sortField = (sort == null || sort.isBlank()) ? "id" : sort;
        
        // Sanitize sort field to prevent injection - only allow alphanumeric and dots/underscores
        if (!sortField.matches("^[a-zA-Z0-9_.]+$")) {
            throw new IllegalArgumentException("Invalid sort field: " + sortField);
        }
        
        Sort sortOrder = Sort.by(direction, sortField);
        
        return PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize), sortOrder);
    }

    public static HttpHeaders createContentRangeHeaders(int start, int end, Page<?> page, String resourceName) {
        HttpHeaders headers = new HttpHeaders();
        long totalElements = page.getTotalElements();
        headers.add("Content-Range", resourceName + " " + start + "-" + Math.min(end, (int)totalElements) + "/" + totalElements);
        headers.add("Access-Control-Expose-Headers", "Content-Range");
        return headers;
    }
}
