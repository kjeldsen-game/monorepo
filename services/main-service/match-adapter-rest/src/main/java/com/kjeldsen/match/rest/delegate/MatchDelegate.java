package com.kjeldsen.match.rest.delegate;

import com.kjeldsen.match.controllers.ValidationException;
import com.kjeldsen.match.engine.Game;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.engine.entities.Match;
import com.kjeldsen.match.engine.entities.MatchReport;
import com.kjeldsen.match.engine.entities.Team;
import com.kjeldsen.match.rest.api.MatchApiDelegate;
import com.kjeldsen.match.rest.model.CreateMatchRequest;
import com.kjeldsen.match.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MatchDelegate implements MatchApiDelegate {

    private final TeamRepository teamRepository;

    @Override
    public ResponseEntity<Void> createMatch(CreateMatchRequest createMatchRequest) {
        Team home = teamRepository.findById((long) createMatchRequest.getHome().getId())
            .orElseThrow(() -> new ValidationException("Home team not found"));

        Team away = teamRepository.findById((long) createMatchRequest.getHome().getId())
            .orElseThrow(() -> new ValidationException("Away team not found"));

        Match match = Match.builder()
            .home(home)
            .away(away)
            .build();

        GameState state = Game.play(match);
        MatchReport report = new MatchReport(state, state.getPlays(), home, away);
        match.setMatchReport(report);

        String json = JsonUtils.exclude(
            report, "plays.duel.initiator.skills", "plays.duel.challenger.skills");

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
