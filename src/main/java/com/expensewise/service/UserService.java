package com.expensewise.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.expensewise.entity.User;
import com.expensewise.repository.UserRepository;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    public Page<User> getAllPaginated(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public User getById(Long id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid user ID requested: {}", id);
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        return repo.findById(id)
            .orElseThrow(() -> {
                logger.error("User not found with ID: {}", id);
                return new RuntimeException("User not found: " + id);
            });
    }

    public User create(User user) {
        if (user == null) {
            logger.warn("Attempt to create null user");
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            logger.warn("User username is empty");
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            logger.warn("User email is empty");
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (repo.existsByUsername(user.getUsername())) {
            logger.warn("Attempt to create user with existing username: {}", user.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }
        logger.info("Creating new user: {}", user.getUsername());
        return repo.save(user);
    }

    public User update(Long id, User updated) {
        if (id == null || id <= 0) {
            logger.warn("Invalid user ID for update: {}", id);
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        if (updated == null) {
            logger.warn("Attempt to update with null user");
            throw new IllegalArgumentException("Updated user cannot be null");
        }
        
        User existing = getById(id);
        
        if (updated.getUsername() != null && !updated.getUsername().isBlank()) {
            if (!existing.getUsername().equals(updated.getUsername()) && repo.existsByUsername(updated.getUsername())) {
                logger.warn("Attempt to update user with existing username: {}", updated.getUsername());
                throw new IllegalArgumentException("Username already exists");
            }
            existing.setUsername(updated.getUsername());
        }
        if (updated.getEmail() != null && !updated.getEmail().isBlank()) {
            existing.setEmail(updated.getEmail());
        }
        
        logger.info("Updated user with ID: {}", id);
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid user ID for deletion: {}", id);
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        if (!repo.existsById(id)) {
            logger.error("Attempt to delete non-existent user with ID: {}", id);
            throw new RuntimeException("User not found: " + id);
        }
        logger.info("Deleting user with ID: {}", id);
        repo.deleteById(id);
    }
}