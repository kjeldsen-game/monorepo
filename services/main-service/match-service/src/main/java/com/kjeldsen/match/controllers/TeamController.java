package com.kjeldsen.match.controllers;

import com.kjeldsen.match.engine.TeamGenerator;
import com.kjeldsen.match.engine.entities.PlayerPosition;
import com.kjeldsen.match.engine.modifers.HorizontalPressure;
import com.kjeldsen.match.engine.modifers.Tactic;
import com.kjeldsen.match.engine.modifers.VerticalPressure;
import com.kjeldsen.match.models.Team;
import com.kjeldsen.match.repositories.TeamRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
public class TeamController {

    private final TeamRepository teamRepository;

    // Called by "New Opponent" button
    @GetMapping("/teams/random")
    public ResponseEntity<Team> random() {
        Team team = teamRepository.save(TeamGenerator.randomTeam());
        return ResponseEntity.ok(team);
    }

    // Called by "Create Team" button — receives {players: [{position: "GOALKEEPER"}, ...]}
    @PostMapping("/teams")
    public ResponseEntity<Team> create(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Map<String, String>> playerDefs = (List<Map<String, String>>) body.get("players");
        List<PlayerPosition> positions = playerDefs.stream()
            .map(p -> PlayerPosition.valueOf(p.get("position")))
            .collect(Collectors.toList());
        Team team = teamRepository.save(TeamGenerator.teamFromPositions(positions));
        UserController.currentHomeTeamId.set(team.getId());
        return ResponseEntity.ok(team);
    }

    // Called by tactic form save
    @PatchMapping("/teams/{id}")
    public ResponseEntity<Team> updateTactic(
        @PathVariable Long id,
        @RequestBody Map<String, String> body) {

        Team team = teamRepository.findById(id)
            .orElseThrow(() -> new ValidationException("Team not found"));

        Team updated = Team.builder()
            .id(team.getId())
            .players(team.getPlayers())
            .rating(team.getRating())
            .tactic(Tactic.valueOf(body.get("tactic")))
            .verticalPressure(VerticalPressure.valueOf(body.get("verticalPressure")))
            .horizontalPressure(HorizontalPressure.valueOf(body.get("horizontalPressure")))
            .build();

        teamRepository.save(updated);
        return ResponseEntity.ok(updated);
    }
}
