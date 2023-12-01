package com.kjeldsen.match.controllers;

import com.kjeldsen.match.models.User;
import com.kjeldsen.match.security.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/token")
    public ResponseEntity<?> token(@RequestBody User user, HttpServletResponse res) {
        String token = authService.signin(user.getEmail(), user.getPassword(), res);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
