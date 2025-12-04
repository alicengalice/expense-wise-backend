package com.expensewise.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.expensewise.entity.User;
import com.expensewise.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    public User getById(Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    public User create(User user) {
        return repo.save(user);
    }

    public User update(Long id, User updated) {
        User existing = getById(id);
        existing.setUsername(updated.getUsername());
        existing.setEmail(updated.getEmail());
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}