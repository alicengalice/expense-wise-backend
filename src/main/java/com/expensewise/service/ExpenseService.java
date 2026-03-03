package com.expensewise.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.expensewise.dto.mapper.ExpenseMapper;
import com.expensewise.dto.request.ExpenseCreateDTO;
import com.expensewise.dto.request.ExpenseUpdateDTO;
import com.expensewise.dto.response.ExpenseResponseDTO;
import com.expensewise.dto.response.ExpenseSummaryDTO;
import com.expensewise.entity.Category;
import com.expensewise.entity.Expense;
import com.expensewise.entity.User;
import com.expensewise.repository.CategoryRepository;
import com.expensewise.repository.ExpenseRepository;
import com.expensewise.repository.UserRepository;

@Service
public class ExpenseService {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseService.class);
    private final ExpenseRepository expenseRepo;
    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;
    private final ExpenseMapper mapper;

    public ExpenseService(
            ExpenseRepository expenseRepo,
            CategoryRepository categoryRepo,
            UserRepository userRepo,
            ExpenseMapper mapper
    ) {
        this.expenseRepo = expenseRepo;
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
        this.mapper = mapper;
    }

    public List<ExpenseResponseDTO> getAll() {
        List<Expense> expenses = expenseRepo.findAll();
        return expenses.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Page<ExpenseResponseDTO> getAllPaginated(Pageable pageable) {
        Page<Expense> page = expenseRepo.findAll(pageable);
        return page.map(mapper::toResponseDTO);
    }

    public ExpenseResponseDTO getById(Long id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid expense ID requested: {}", id);
            throw new IllegalArgumentException("Expense ID must be a positive number");
        }
        Expense expense = expenseRepo.findById(id)
            .orElseThrow(() -> {
                logger.error("Expense not found with ID: {}", id);
                return new RuntimeException("Expense not found: " + id);
            });
        return mapper.toResponseDTO(expense);
    }

    public ExpenseResponseDTO create(ExpenseCreateDTO createDTO) {
        if (createDTO == null) {
            logger.warn("Attempt to create null expense");
            throw new IllegalArgumentException("Expense cannot be null");
        }
        
        // Validate and fetch category
        Category category = categoryRepo.findById(createDTO.getCategoryId())
            .orElseThrow(() -> {
                logger.error("Category not found with ID: {}", createDTO.getCategoryId());
                return new RuntimeException("Category not found: " + createDTO.getCategoryId());
            });
        
        // Validate and fetch user
        User user = userRepo.findById(createDTO.getUserId())
            .orElseThrow(() -> {
                logger.error("User not found with ID: {}", createDTO.getUserId());
                return new RuntimeException("User not found: " + createDTO.getUserId());
            });
        
        // Map DTO to entity
        Expense expense = mapper.toEntity(createDTO);
        expense.setCategory(category);
        expense.setUser(user);
        
        logger.info("Creating new expense with description: {}", expense.getDescription());
        Expense saved = expenseRepo.save(expense);
        return mapper.toResponseDTO(saved);
    }

    public ExpenseResponseDTO update(Long id, ExpenseUpdateDTO updateDTO) {
        if (id == null || id <= 0) {
            logger.warn("Invalid expense ID for update: {}", id);
            throw new IllegalArgumentException("Expense ID must be a positive number");
        }
        if (updateDTO == null) {
            logger.warn("Attempt to update with null expense");
            throw new IllegalArgumentException("Updated expense cannot be null");
        }
        
        // Fetch existing expense
        Expense existing = expenseRepo.findById(id)
            .orElseThrow(() -> {
                logger.error("Expense not found with ID: {}", id);
                return new RuntimeException("Expense not found: " + id);
            });

        // Update simple fields using mapper (handles nulls automatically)
        mapper.updateEntityFromDTO(updateDTO, existing);

        // Handle category update if provided
        if (updateDTO.getCategoryId() != null) {
            Category category = categoryRepo.findById(updateDTO.getCategoryId())
                .orElseThrow(() -> {
                    logger.error("Category not found with ID: {}", updateDTO.getCategoryId());
                    return new RuntimeException("Category not found: " + updateDTO.getCategoryId());
                });
            existing.setCategory(category);
        }
        
        // Handle user update if provided
        if (updateDTO.getUserId() != null) {
            User user = userRepo.findById(updateDTO.getUserId())
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", updateDTO.getUserId());
                    return new RuntimeException("User not found: " + updateDTO.getUserId());
                });
            existing.setUser(user);
        }

        logger.info("Updated expense with ID: {}", id);
        Expense saved = expenseRepo.save(existing);
        return mapper.toResponseDTO(saved);
    }

    public void delete(Long id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid expense ID for deletion: {}", id);
            throw new IllegalArgumentException("Expense ID must be a positive number");
        }
        if (!expenseRepo.existsById(id)) {
            logger.error("Attempt to delete non-existent expense with ID: {}", id);
            throw new RuntimeException("Expense not found: " + id);
        }
        logger.info("Deleting expense with ID: {}", id);
        expenseRepo.deleteById(id);
    }

    public ExpenseSummaryDTO getDailyExpenseSummary(Long userId, LocalDate date) {
        List<Expense> expenses = expenseRepo.findByUserIdAndDate(userId, date);

        BigDecimal totalAmount = expenses.stream()
            .map(Expense::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ExpenseSummaryDTO(date, "daily", totalAmount, expenses.size(), date, date);
    }


    public ExpenseSummaryDTO getWeeklyExpenseSummary(Long userId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        if (endDate.minusDays(6).isAfter(startDate)) {
            throw new IllegalArgumentException("Day range cannot exceed 7 days");
        }

        List<Expense> expenses = expenseRepo.findByUserIdAndDateBetween(userId, startDate, endDate);

        BigDecimal totalAmount = expenses.stream()
            .map(Expense::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ExpenseSummaryDTO(startDate, "weekly", totalAmount, expenses.size(), startDate, endDate);
    }

    public ExpenseSummaryDTO getMonthlyExpenseSummary(Long userId, LocalDate date) {
        LocalDate monthStart = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthEnd = date.with(TemporalAdjusters.lastDayOfMonth());

        List<Expense> expenses = expenseRepo.findByUserIdAndDateBetween(userId, monthStart, monthEnd);

        BigDecimal totalAmount = expenses.stream()
            .map(Expense::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ExpenseSummaryDTO(date, "monthly", totalAmount, expenses.size(), monthStart, monthEnd);
    }

}