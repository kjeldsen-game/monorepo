package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.application.events.MatchStartedEvent;
import com.kjeldsen.match.engine.Game;
import com.kjeldsen.match.engine.exceptions.GameStateException;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.entities.Id;
import com.kjeldsen.match.entities.Match;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatchStartUseCase {

    public void start(Id matchId) {
        log.info("Starting match {}", matchId);

        MatchStartedEvent matchStartedEvent = MatchStartedEvent.builder()
            .matchId(Id.generate())
            .build();

        // TODO - the match data (teams, players, etc.) should be retrieved for the engine to use
        Match match = Match.generate(matchStartedEvent);

        GameState endState;
        try {
            endState = Game.play(match);
        } catch (GameStateException exception) {
            log.error(exception.getMessage());
            // TODO - exception handling for external calls
            return;
        }

        processGame(endState);
    }

    private void processGame(GameState state) {
        // TODO - Once the game is over, any resources (use cases, events) can be created and saved
    }
}
