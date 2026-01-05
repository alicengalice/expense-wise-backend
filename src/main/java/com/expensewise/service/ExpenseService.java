package com.expensewise.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.expensewise.entity.Expense;
import com.expensewise.repository.ExpenseRepository;

@Service
public class ExpenseService {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseService.class);
    private final ExpenseRepository repo;

    public ExpenseService(ExpenseRepository repo) {
        this.repo = repo;
    }

    public List<Expense> getAll() {
        return repo.findAll();
    }

    public Page<Expense> getAllPaginated(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Expense getById(Long id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid expense ID requested: {}", id);
            throw new IllegalArgumentException("Expense ID must be a positive number");
        }
        return repo.findById(id)
            .orElseThrow(() -> {
                logger.error("Expense not found with ID: {}", id);
                return new RuntimeException("Expense not found: " + id);
            });
    }

    public Expense create(Expense expense) {
        if (expense == null) {
            logger.warn("Attempt to create null expense");
            throw new IllegalArgumentException("Expense cannot be null");
        }
        if (expense.getAmount() == null || expense.getAmount().signum() <= 0) {
            logger.warn("Invalid amount for expense: {}", expense.getAmount());
            throw new IllegalArgumentException("Expense amount must be greater than 0");
        }
        if (expense.getDescription() == null || expense.getDescription().isBlank()) {
            logger.warn("Expense description is empty");
            throw new IllegalArgumentException("Expense description cannot be empty");
        }
        logger.info("Creating new expense with description: {}", expense.getDescription());
        return repo.save(expense);
    }

    public Expense update(Long id, Expense updated) {
        if (id == null || id <= 0) {
            logger.warn("Invalid expense ID for update: {}", id);
            throw new IllegalArgumentException("Expense ID must be a positive number");
        }
        if (updated == null) {
            logger.warn("Attempt to update with null expense");
            throw new IllegalArgumentException("Updated expense cannot be null");
        }
        
        Expense existing = getById(id);

        if (updated.getDescription() != null && !updated.getDescription().isBlank()) {
            existing.setDescription(updated.getDescription());
        }
        if (updated.getAmount() != null && updated.getAmount().signum() > 0) {
            existing.setAmount(updated.getAmount());
        }
        if (updated.getCategory() != null) {
            existing.setCategory(updated.getCategory());
        }
        if (updated.getUser() != null) {
            existing.setUser(updated.getUser());
        }
        if (updated.getDate() != null) {
            existing.setDate(updated.getDate());
        }

        logger.info("Updated expense with ID: {}", id);
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid expense ID for deletion: {}", id);
            throw new IllegalArgumentException("Expense ID must be a positive number");
        }
        if (!repo.existsById(id)) {
            logger.error("Attempt to delete non-existent expense with ID: {}", id);
            throw new RuntimeException("Expense not found: " + id);
        }
        logger.info("Deleting expense with ID: {}", id);
        repo.deleteById(id);
    }
}