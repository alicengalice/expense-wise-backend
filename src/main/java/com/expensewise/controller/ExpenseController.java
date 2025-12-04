package com.expensewise.controller;

import java.util.List;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.expensewise.entity.Expense;
import com.expensewise.service.ExpenseService;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    // ---- LIST WITH PAGINATION ----
    @GetMapping
    public ResponseEntity<List<Expense>> getAll(
            @RequestParam(value = "_start", defaultValue = "0") int start,
            @RequestParam(value = "_end", defaultValue = "10") int end
    ) {
        List<Expense> all = service.getAll();
        int total = all.size();

        List<Expense> page = all.subList(
                Math.min(start, total),
                Math.min(end, total)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", "expenses " + start + "-" + end + "/" + total);
        headers.add("Access-Control-Expose-Headers", "Content-Range");

        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    // ---- GET ONE ----
    @GetMapping("/{id}")
    public Expense getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // ---- CREATE ----
    @PostMapping
    public Expense create(@RequestBody Expense expense) {
        return service.create(expense);
    }

    // ---- UPDATE ----
    @PutMapping("/{id}")
    public Expense update(@PathVariable Long id, @RequestBody Expense expense) {
        return service.update(id, expense);
    }

    // ---- DELETE ----
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}