package com.kjeldsen.match.controllers;

import static org.springframework.http.ResponseEntity.ok;

import com.kjeldsen.match.engine.Game;
import com.kjeldsen.match.engine.processing.Report;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.models.Match;
import com.kjeldsen.match.models.Team;
import com.kjeldsen.match.repositories.MatchRepository;
import com.kjeldsen.match.repositories.TeamRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MatchController {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    @PostMapping("/matches")
    public ResponseEntity<?> create(@RequestBody Match _match) {
        Team home = teamRepository.findById(1L)
            .orElseThrow(() -> new ValidationException("You need to create a team first"));

        Team away = teamRepository.findById(teamRepository.count())
            .map(team -> {
                if (Objects.equals(team.getId(), home.getId())) {
                    throw new ValidationException("You need to create your opponent's team");
                }
                return team;
            }).orElseThrow();

        Match match = Match.builder()
            .home(home)
            .away(away)
            .build();
        matchRepository.save(match);

        GameState state = Game.play(match);
        Report report = new Report(state, home, away);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/matches/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) {
        matchRepository.findById(id);
        return ok().build();
    }
}
