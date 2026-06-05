package com.kjeldsen.match.controllers;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    // Tracks the ID of the most recently created home team so the match request can look it up
    public static final AtomicLong currentHomeTeamId = new AtomicLong(0);

    @GetMapping("/users/me")
    public ResponseEntity<Map<String, Object>> me() {
        long teamId = currentHomeTeamId.get();
        return ResponseEntity.ok(Map.of(
            "id", 1,
            "name", "Home Manager",
            "team", Map.of("id", teamId)
        ));
    }
}
