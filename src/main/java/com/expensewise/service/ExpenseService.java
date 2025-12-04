package com.expensewise.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.expensewise.entity.Expense;
import com.expensewise.repository.ExpenseRepository;

@Service
public class ExpenseService {
    private final ExpenseRepository repo;

    public ExpenseService(ExpenseRepository repo) {
        this.repo = repo;
    }

    public List<Expense> getAll() {
        return repo.findAll();
    }

    public Expense getById(Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Expense not found: " + id));
    }

    public Expense create(Expense expense) {
        return repo.save(expense);
    }

    public Expense update(Long id, Expense updated) {
        Expense existing = getById(id);

        existing.setDescription(updated.getDescription());
        existing.setAmount(updated.getAmount());
        existing.setCategory(updated.getCategory());
        existing.setUser(updated.getUser());
        existing.setDate(updated.getDate());

        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}