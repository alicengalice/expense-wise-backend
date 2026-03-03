package com.expensewise.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.expensewise.dto.request.ExpenseCreateDTO;
import com.expensewise.dto.request.ExpenseUpdateDTO;
import com.expensewise.dto.response.ExpenseResponseDTO;
import com.expensewise.dto.response.ExpenseSummaryDTO;
import com.expensewise.service.ExpenseService;
import com.expensewise.util.PaginationUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);
    private static final String RESOURCE_NAME = "expenses";
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getAll(
            @RequestParam(value = "_start", defaultValue = "0") int start,
            @RequestParam(value = "_end", defaultValue = "10") int end,
            @RequestParam(value = "_sort", required = false) String sort,
            @RequestParam(value = "_order", defaultValue = "ASC") String order
    ) {
        logger.warn("Invalid pagination parameters: start={}, end={}", start, end);
        Pageable pageable = PaginationUtil.createPageable(start, end, sort, order);
        Page<ExpenseResponseDTO> page = expenseService.getAllPaginated(pageable);
        HttpHeaders headers = PaginationUtil.createContentRangeHeaders(start, end, page, RESOURCE_NAME);

        logger.info("Retrieved expenses page: start={}, end={}, total={}", start, end, page.getTotalElements());
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getById(@PathVariable Long id) {
        logger.info("Fetching expense with ID: {}", id);
        ExpenseResponseDTO expense = expenseService.getById(id);
        return ResponseEntity.ok(expense);
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> create(@Valid @RequestBody ExpenseCreateDTO expenseCreateDTO) {
        logger.info("Creating new expense");
        ExpenseResponseDTO created = expenseService.create(expenseCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ExpenseUpdateDTO expenseUpdateDTO) {
        logger.info("Updating expense with ID: {}", id);
        ExpenseResponseDTO updated = expenseService.update(id, expenseUpdateDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting expense with ID: {}", id);
        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary/daily/{userId}/{date}")
    public ResponseEntity<ExpenseSummaryDTO> getDailyExpenseSummary(
        @PathVariable Long userId,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            ExpenseSummaryDTO summary = expenseService.getDailyExpenseSummary(userId, date);
            return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary/weekly/{userId}")
    public ResponseEntity<ExpenseSummaryDTO> getWeeklyExpenseSummary(
        @PathVariable Long userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
            ExpenseSummaryDTO summary = expenseService.getWeeklyExpenseSummary(userId, startDate, endDate);
            return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary/monthly/{userId}/{date}")
    public ResponseEntity<ExpenseSummaryDTO> getMonthlyExpenseSummary(
        @PathVariable Long userId,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            ExpenseSummaryDTO summary = expenseService.getMonthlyExpenseSummary(userId, date);
            return ResponseEntity.ok(summary);
    }
}