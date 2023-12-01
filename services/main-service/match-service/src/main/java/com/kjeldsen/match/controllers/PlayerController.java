package com.kjeldsen.match.controllers;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

import com.kjeldsen.match.security.AuthService;
import com.kjeldsen.match.models.Player;
import com.kjeldsen.match.models.User;
import com.kjeldsen.match.repositories.PlayerRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    private final AuthService authService;

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
    public ResponseEntity<?> update(
        @PathVariable Long id, @RequestBody Player newPlayer, Authentication auth) {
        User user = authService.getUser(auth);

        Player existingPlayer = playerRepository.findById(id)
            .orElseThrow(() -> new ValidationException("Player not found"));

        if (!Objects.equals(existingPlayer.getTeamId(), user.getTeam().getId())) {
            throw new ValidationException("Player is not on your team");
        }
        if (newPlayer.getPosition() != null) {
            existingPlayer.setPosition(newPlayer.getPosition());
        }
        if (newPlayer.getPlayerOrder() != null) {
            existingPlayer.setPlayerOrder(newPlayer.getPlayerOrder());
        }

        playerRepository.save(existingPlayer);
        return ok(existingPlayer);
    }
}
