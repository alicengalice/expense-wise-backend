package com.expensewise.controller;

import java.util.List;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.expensewise.entity.User;
import com.expensewise.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // ---- LIST WITH PAGINATION ----
    @GetMapping
    public ResponseEntity<List<User>> getAll(
            @RequestParam(value = "_start", defaultValue = "0") int start,
            @RequestParam(value = "_end", defaultValue = "10") int end
    ) {
        List<User> all = service.getAll();
        int total = all.size();

        List<User> page = all.subList(
                Math.min(start, total),
                Math.min(end, total)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", "users " + start + "-" + end + "/" + total);
        headers.add("Access-Control-Expose-Headers", "Content-Range");

        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    // ---- GET ONE ----
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // ---- CREATE ----
    @PostMapping
    public User create(@RequestBody User user) {
        return service.create(user); // MUST return created user with id
    }

    // ---- UPDATE ----
    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        return service.update(id, user);
    }

    // ---- DELETE ----
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}