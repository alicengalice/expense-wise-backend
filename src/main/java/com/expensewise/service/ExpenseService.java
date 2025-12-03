package com.expensewise.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.expensewise.entity.Category;
import com.expensewise.entity.Expense;
import com.expensewise.entity.User;
import com.expensewise.repository.ExpenseRepository;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    public ExpenseService(ExpenseRepository expenseRepository, UserService userService,
            CategoryService categoryService) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found: " + id));
    }

    public Expense create(Expense expense, Long userId, Long categoryId) {
        User user = userService.getById(userId);
        Category category = categoryService.getById(categoryId);
        expense.setUser(user);
        expense.setCategory(category);
        return expenseRepository.save(expense);
    }

    public Expense update(Long id, Expense updated, Long userId, Long categoryId) {
        Expense existing = getById(id);

        existing.setAmount(updated.getAmount());
        existing.setDescription(updated.getDescription());
        existing.setDate(updated.getDate());

        if (userId != null) {
            existing.setUser(userService.getById(userId));
        }

        if (categoryId != null) {
            existing.setCategory(categoryService.getById(categoryId));
        }

        return expenseRepository.save(existing);
    }

    public void delete(Long id) {
        expenseRepository.deleteById(id);
    }
    
}
