package com.expensewise.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.expensewise.dto.mapper.CategoryMapper;
import com.expensewise.dto.request.CategoryCreateDTO;
import com.expensewise.dto.response.CategoryResponseDTO;
import com.expensewise.entity.Category;
import com.expensewise.repository.CategoryRepository;

@Service
public class CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(mapper::toCategoryResponseDTO)
                .collect(Collectors.toList());
    }

    public Page<CategoryResponseDTO> getAllPaginated(Pageable pageable) {
        Page<Category> page = categoryRepository.findAll(pageable);
        return page.map(mapper::toCategoryResponseDTO);
    }

    public CategoryResponseDTO getById(Long id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid category ID requested: {}", id);
            throw new IllegalArgumentException("Category ID must be a positive number");
        }
        Category category = categoryRepository.findById(id).orElseThrow(() -> {
            logger.error("Category not found with ID: {}", id);
            return new RuntimeException("Category not found: " + id);
        });
        return mapper.toCategoryResponseDTO(category);
    }

    public CategoryResponseDTO create(CategoryCreateDTO createDTO) {
        if (createDTO == null) {
            logger.warn("Attempt to create null category");
            throw new IllegalArgumentException("Category cannot be null");
        }
        
        // Map DTO to entity
        Category category = mapper.toEntity(createDTO);
        
        logger.info("Creating new category: {}", category.getName());
        Category saved = categoryRepository.save(category);
        return mapper.toCategoryResponseDTO(saved);
    }

    public CategoryResponseDTO update(Long id, CategoryCreateDTO updateDTO) {
        if (id == null || id <= 0) {
            logger.warn("Invalid category ID for update: {}", id);
            throw new IllegalArgumentException("Category ID must be a positive number");
        }
        if (updateDTO == null) {
            logger.warn("Attempt to update with null category");
            throw new IllegalArgumentException("Updated category cannot be null");
        }
        
        // Fetch existing category
        Category existing = categoryRepository.findById(id).orElseThrow(() -> {
            logger.error("Category not found with ID: {}", id);
            return new RuntimeException("Category not found: " + id);
        });
        
        // Update fields (could also use CategoryUpdateDTO for partial updates)
        if (updateDTO.getName() != null && !updateDTO.getName().isBlank()) {
            existing.setName(updateDTO.getName());
        }
        if (updateDTO.getDescription() != null) {
            existing.setDescription(updateDTO.getDescription());
        }
        
        logger.info("Updated category with ID: {}", id);
        Category saved = categoryRepository.save(existing);
        return mapper.toCategoryResponseDTO(saved);
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
