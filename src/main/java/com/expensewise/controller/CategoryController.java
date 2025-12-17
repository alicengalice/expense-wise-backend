package com.expensewise.controller;

import java.util.List;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.expensewise.entity.Category;
import com.expensewise.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll(
            @RequestParam(value = "_start", defaultValue = "0") int start,
            @RequestParam(value = "_end", defaultValue = "10") int end
    ) {
        List<Category> all = service.getAllCategories();
        int total = all.size();

        List<Category> page = all.subList(
                Math.min(start, total),
                Math.min(end, total)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", "categories " + start + "-" + end + "/" + total);
        headers.add("Access-Control-Expose-Headers", "Content-Range");

        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Category getOne(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Category create(@RequestBody Category category) {
        return service.create(category);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable Long id, @RequestBody Category category) {
        return service.update(id, category);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}