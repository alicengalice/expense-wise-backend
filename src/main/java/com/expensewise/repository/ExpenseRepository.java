package com.expensewise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expensewise.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser_UsernameOrderByDateDesc(String username);
}
