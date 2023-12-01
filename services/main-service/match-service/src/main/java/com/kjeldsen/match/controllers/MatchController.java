package com.kjeldsen.match.controllers;

import com.kjeldsen.match.engine.Game;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.models.Match;
import com.kjeldsen.match.models.MatchReport;
import com.kjeldsen.match.models.Team;
import com.kjeldsen.match.repositories.TeamRepository;
import com.kjeldsen.match.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MatchController {

    private final TeamRepository teamRepository;

    @PostMapping(value = "/matches", produces = "application/json")
    public ResponseEntity<?> create(@RequestBody Match matchParams) {
        // TODO - teams need to be read from mongo
        Team home = teamRepository.findById(matchParams.getHome().getId())
            .orElseThrow(() -> new ValidationException("Home team not found"));

        Team away = teamRepository.findById(matchParams.getHome().getId())
            .orElseThrow(() -> new ValidationException("Away team not found"));

        // TODO - modifiers here
        Match match = Match.builder()
            .home(home)
            .away(away)
            .build();

        GameState state = Game.play(match);
        MatchReport report = new MatchReport(state, state.getPlays(), home, away);
        match.setMatchReport(report);

        // TODO - save match/report
        String json = JsonUtils.exclude(
            report, "plays.duel.initiator.skills", "plays.duel.challenger.skills");

        return ResponseEntity.ok(json);
    }
}
