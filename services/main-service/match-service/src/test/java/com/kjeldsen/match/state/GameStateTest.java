package com.kjeldsen.match.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.kjeldsen.match.engine.RandomHelper;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.player.domain.PitchArea;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class GameStateTest {

    @Test
    void initialGameStateNotNull() {
        Team home = RandomHelper.genTeam();
        Team away = RandomHelper.genTeam();
        Match match = Match.builder()
            .home(home)
            .away(away)
            .build();

        GameState gameState = GameState.init(match);
        assertNotNull(gameState);
        assertNotNull(gameState.getTurn());
        assertEquals(0, gameState.getClock());
        assertNotNull(gameState.getHome());
        assertNotNull(gameState.getAway());
        assertNotNull(gameState.getBallState());
        assertNotNull(gameState.getPlays());
    }

    @Test
    void initialBallStateNotNull() {
        BallState ballState = BallState.init();
        assertNotNull(ballState);
    }

    @Test
    void initialTeamStateNotNull() {
        Team team = RandomHelper.genTeam();
        TeamState teamState = TeamState.init(team);
        assertNotNull(teamState);
        assertEquals(0, teamState.getScore());
        assertNotNull(teamState.getPlayers());
    }

    @Test
    void ballWithoutPlayerAfterKickOffFails() {
        Team home = RandomHelper.genTeam();
        Team away = RandomHelper.genTeam();
        Match match = Match.builder()
            .home(home)
            .away(away)
            .build();

        GameState state = GameState.init(match);

        assertThrows(GameStateException.class,
            () -> Optional.of(state)
                .map((before) ->
                    GameState.builder()
                        .turn(before.getTurn())
                        .clock(1)
                        .home(before.getHome())
                        .away(before.getAway())
                        .ballState((new BallState(null, PitchArea.CENTRE_MIDFIELD, BallHeight.GROUND)))
                        .plays(before.getPlays())
                        .build())
                .orElse(state));
    }
}