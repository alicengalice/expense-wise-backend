package com.expensewise.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.expensewise.entity.Category;
import com.expensewise.repository.CategoryRepository;

@Service
public class CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Page<Category> getAllPaginated(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Category getById(Long id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid category ID requested: {}", id);
            throw new IllegalArgumentException("Category ID must be a positive number");
        }
        return categoryRepository.findById(id).orElseThrow(() -> {
            logger.error("Category not found with ID: {}", id);
            return new RuntimeException("Category not found: " + id);
        });
    }

    public Category create(Category category) {
        if (category == null) {
            logger.warn("Attempt to create null category");
            throw new IllegalArgumentException("Category cannot be null");
        }
        if (category.getName() == null || category.getName().isBlank()) {
            logger.warn("Category name is empty");
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        logger.info("Creating new category: {}", category.getName());
        return categoryRepository.save(category);
    }

    public Category update(Long id, Category updated) {
        if (id == null || id <= 0) {
            logger.warn("Invalid category ID for update: {}", id);
            throw new IllegalArgumentException("Category ID must be a positive number");
        }
        if (updated == null) {
            logger.warn("Attempt to update with null category");
            throw new IllegalArgumentException("Updated category cannot be null");
        }
        
        Category existing = getById(id);
        if (updated.getName() != null && !updated.getName().isBlank()) {
            existing.setName(updated.getName());
        }
        logger.info("Updated category with ID: {}", id);
        return categoryRepository.save(existing);
    }

    public void delete(Long id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid category ID for deletion: {}", id);
            throw new IllegalArgumentException("Category ID must be a positive number");
        }
        if (!categoryRepository.existsById(id)) {
            logger.error("Attempt to delete non-existent category with ID: {}", id);
            throw new RuntimeException("Category not found: " + id);
        }
        logger.info("Deleting category with ID: {}", id);
        categoryRepository.deleteById(id);
    }
}
