package com.expensewise.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.expensewise.entity.Expense;
import com.expensewise.service.ExpenseService;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @GetMapping("/{id}/")
    public Expense getById(@PathVariable Long id) {
        return expenseService.getById(id);
    }

    @PostMapping
    public Expense create(@RequestBody Expense expense, @RequestParam Long userId, @RequestParam Long categoryId) {
        return expenseService.create(expense, userId, categoryId);
    }

    @PutMapping("/{id}") 
    public Expense update(@PathVariable Long id, 
                        @RequestBody Expense expense, 
                        @RequestParam(required = false) Long userId,
                        @RequestParam(required = false) Long categoryId) {
        return expenseService.update(id, expense, userId, categoryId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        expenseService.delete(id);
    }
}
