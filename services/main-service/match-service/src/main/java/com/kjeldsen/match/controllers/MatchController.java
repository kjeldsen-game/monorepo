package com.kjeldsen.match.controllers;

import static org.springframework.http.ResponseEntity.ok;

import com.kjeldsen.match.engine.Game;
import com.kjeldsen.match.engine.processing.ReportService;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.models.Match;
import com.kjeldsen.match.models.MatchReport;
import com.kjeldsen.match.models.Team;
import com.kjeldsen.match.models.User;
import com.kjeldsen.match.repositories.MatchRepository;
import com.kjeldsen.match.repositories.TeamRepository;
import com.kjeldsen.match.security.AuthService;
import com.kjeldsen.match.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    private final AuthService authService;
    private final ReportService reportService;

    @PostMapping(value = "/matches", produces = "application/json")
    public ResponseEntity<?> create(@RequestBody Match matchParams, Authentication auth) {
        User user = authService.getUser(auth);
        Team home = user.getTeam();

        Team away = teamRepository.findById(matchParams.getAway().getId())
            .orElseThrow(() -> new ValidationException("You need to create your opponent's team"));

        validateTeam(home);
        validateTeam(away);

        Match match = Match.builder()
            .home(home)
            .away(away)
            .build();
        // TODO - this should be saved ahead of time and played at a scheduled date and time
        matchRepository.save(match);

        GameState state = Game.play(match);
        MatchReport report = reportService.generateReport(state, home, away);

        match.setMatchReport(report);
//        matchRepository.save(match);

        String json = JsonUtils.exclude(
            report, "plays.duel.initiator.skills", "plays.duel.challenger.skills");

        return ResponseEntity.ok(json);
    }

    private void validateTeam(Team team) {
        if (team == null) {
            throw new ValidationException("You need to create a team");
        }
        if (team.getTactic() == null
            || team.getHorizontalPressure() == null
            || team.getVerticalPressure() == null) {

            throw new ValidationException("You need to set your team's tactics");
        }
    }

    @GetMapping("/matches/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) {
        matchRepository.findById(id);
        return ok().build();
    }
}
