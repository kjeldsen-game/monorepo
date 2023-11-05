package com.kjeldsen.match.controllers;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import com.kjeldsen.match.models.Player;
import com.kjeldsen.match.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PlayerController {

    private final PlayerRepository playerRepository;

    @PostMapping("/players")
    public ResponseEntity<?> create(@RequestBody Player player) {
        // Disabled - players are created as part of a team
        return badRequest().build();
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) {
        playerRepository.findById(id);
        return ok().build();
    }

    @PatchMapping("/players/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Player player) {
        return playerRepository.findById(id)
            .map(p -> {
                p.setPosition(player.getPosition());
                playerRepository.save(p);
                return ok(p);
            })
            .orElseGet(() -> notFound().build());
    }
}
