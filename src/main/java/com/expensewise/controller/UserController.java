package com.expensewise.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.expensewise.entity.User;
import com.expensewise.service.UserService;
import com.expensewise.util.PaginationUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String RESOURCE_NAME = "users";
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll(
            @RequestParam(value = "_start", defaultValue = "0") int start,
            @RequestParam(value = "_end", defaultValue = "10") int end,
            @RequestParam(value = "_sort", required = false) String sort,
            @RequestParam(value = "_order", defaultValue = "ASC") String order
    ) {
        Pageable pageable = PaginationUtil.createPageable(start, end, sort, order);
        Page<User> page = service.getAllPaginated(pageable);
        HttpHeaders headers = PaginationUtil.createContentRangeHeaders(start, end, page, RESOURCE_NAME);

        logger.info("Retrieved users page: start={}, end={}, total={}", start, end, page.getTotalElements());
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        logger.info("Fetching user with ID: {}", id);
        User user = service.getById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        logger.info("Creating new user");
        User created = service.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @Valid @RequestBody User user) {
        logger.info("Updating user with ID: {}", id);
        User updated = service.update(id, user);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}