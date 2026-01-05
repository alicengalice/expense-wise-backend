package com.expensewise.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.expensewise.entity.Expense;
import com.expensewise.service.ExpenseService;
import com.expensewise.util.PaginationUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);
    private static final String RESOURCE_NAME = "expenses";
    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAll(
            @RequestParam(value = "_start", defaultValue = "0") int start,
            @RequestParam(value = "_end", defaultValue = "10") int end,
            @RequestParam(value = "_sort", required = false) String sort,
            @RequestParam(value = "_order", defaultValue = "ASC") String order
    ) {
        logger.warn("Invalid pagination parameters: start={}, end={}", start, end);
        Pageable pageable = PaginationUtil.createPageable(start, end, sort, order);
        Page<Expense> page = service.getAllPaginated(pageable);
        HttpHeaders headers = PaginationUtil.createContentRangeHeaders(start, end, page, RESOURCE_NAME);

        logger.info("Retrieved expenses page: start={}, end={}, total={}", start, end, page.getTotalElements());
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getById(@PathVariable Long id) {
        logger.info("Fetching expense with ID: {}", id);
        Expense expense = service.getById(id);
        return ResponseEntity.ok(expense);
    }

    @PostMapping
    public ResponseEntity<Expense> create(@Valid @RequestBody Expense expense) {
        logger.info("Creating new expense");
        Expense created = service.create(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> update(@PathVariable Long id, @Valid @RequestBody Expense expense) {
        logger.info("Updating expense with ID: {}", id);
        Expense updated = service.update(id, expense);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting expense with ID: {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}