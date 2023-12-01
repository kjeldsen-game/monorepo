package com.kjeldsen.match.controllers;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import com.kjeldsen.match.models.User;
import com.kjeldsen.match.repositories.UserRepository;
import com.kjeldsen.match.security.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    private final AuthService authService;

    @PostMapping("/users")
    public ResponseEntity<?> create(@RequestBody User user, HttpServletResponse res) {
        String email = user.getEmail().trim();
        String token = authService.signup(email, user.getPassword(), res);
        log.info("New user registered: {}", user.getEmail());
        return ok(Map.of("token", token));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) {
        return userRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> notFound().build());
    }

    @GetMapping("/users/me")
    public ResponseEntity<?> me(Authentication auth) {
        return Optional.ofNullable(auth)
            .map(Authentication::getName)
            .flatMap(userRepository::findByEmail)
            .map(ResponseEntity::ok)
            .orElseGet(() -> notFound().build());
    }
}
