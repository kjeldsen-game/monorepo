package com.kjeldsen.match.rest.delegate;

import com.kjeldsen.match.controllers.ValidationException;
import com.kjeldsen.match.engine.Game;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.models.Match;
import com.kjeldsen.match.models.MatchReport;
import com.kjeldsen.match.models.Team;
import com.kjeldsen.match.repositories.TeamRepository;
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
        // TODO - teams need to be read from mongo
        Team home = teamRepository.findById((long) createMatchRequest.getHome().getId())
                .orElseThrow(() -> new ValidationException("Home team not found"));

        Team away = teamRepository.findById((long) createMatchRequest.getHome().getId())
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

        // return ResponseEntity.ok(json);
//        CreateMatchUseCase.NewMatch newMatch = CreateMatchMapper.INSTANCE.map(createMatchRequest);
//        newMatch.setTeamId(Team.TeamId.of("NOTEAM")); // TODO change to receive team id in api?
//        createMatchUseCase.create(newMatch);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
