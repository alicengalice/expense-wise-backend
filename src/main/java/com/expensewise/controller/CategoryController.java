package com.expensewise.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.expensewise.entity.Category;
import com.expensewise.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PostMapping
    public Category create(@RequestBody Category category) {
        return categoryService.create(category);
    }

    @PutMapping
    public Category update(@PathVariable Long id,
                            @RequestBody Category category) {
        return categoryService.update(id, category);
    }

    @DeleteMapping
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
