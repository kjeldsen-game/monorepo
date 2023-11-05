package com.kjeldsen.match.controllers;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import com.kjeldsen.match.models.User;
import com.kjeldsen.match.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/users")
    public ResponseEntity<?> create(@RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            throw new ValidationException("Email and password are required");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ValidationException("Email already in use");
        }
        User saved = userRepository.save(user);
        log.info("New user registered: {}", saved.getEmail());
        return ok(saved);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) {
        return userRepository.findById(id)
            .map((user) -> ok().build())
            .orElseGet(() -> notFound().build());
    }
}
