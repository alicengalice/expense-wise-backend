package com.expensewise.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.expensewise.entity.Category;
import com.expensewise.service.CategoryService;
import com.expensewise.util.PaginationUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private static final String RESOURCE_NAME = "categories";
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll(
            @RequestParam(value = "_start", defaultValue = "0") int start,
            @RequestParam(value = "_end", defaultValue = "10") int end,
            @RequestParam(value = "_sort", required = false) String sort,
            @RequestParam(value = "_order", defaultValue = "ASC") String order
    ) {
        Pageable pageable = PaginationUtil.createPageable(start, end, sort, order);
        Page<Category> page = service.getAllPaginated(pageable);
        HttpHeaders headers = PaginationUtil.createContentRangeHeaders(start, end, page, RESOURCE_NAME);

        logger.info("Retrieved categories page: start={}, end={}, total={}", start, end, page.getTotalElements());
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getOne(@PathVariable Long id) {
        logger.info("Fetching category with ID: {}", id);
        Category category = service.getById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<Category> create(@Valid @RequestBody Category category) {
        logger.info("Creating new category");
        Category created = service.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id, @Valid @RequestBody Category category) {
        logger.info("Updating category with ID: {}", id);
        Category updated = service.update(id, category);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting category with ID: {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}