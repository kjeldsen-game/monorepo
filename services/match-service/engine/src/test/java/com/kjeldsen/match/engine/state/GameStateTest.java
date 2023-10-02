package com.kjeldsen.match.engine.state;

import static com.kjeldsen.match.engine.utils.RandomHelper.genTeam;
import static org.junit.jupiter.api.Assertions.*;

import com.kjeldsen.match.domain.aggregate.Match;
import com.kjeldsen.match.domain.aggregate.Team;
import com.kjeldsen.match.domain.type.PitchArea;
import com.kjeldsen.match.engine.exceptions.GameStateException;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class GameStateTest {

    @Test
    void initialGameStateNotNull() {
        Team home = genTeam();
        Team away = genTeam();
        Match match = Match.builder()
            .home(home)
            .away(away)
            .build();

        GameState gameState = GameState.init(match);
        assertNotNull(gameState);
        assertNotNull(gameState.getTurn());
        assertEquals(0, gameState.getClock());
        assertEquals(0, gameState.getAddedTime());
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
        Team team = genTeam();
        TeamState teamState = TeamState.init(team);
        assertNotNull(teamState);
        assertEquals(0, teamState.getScore());
        assertNotNull(teamState.getPlayers());
        assertNotNull(teamState.getPlayerLocation());
        assertNotNull(teamState.getPenaltyCards());
        assertNotNull(teamState.getSubstitutions());
        assertEquals(0, teamState.getFouls());
        assertEquals(0, teamState.getInjuries());
    }

    @Test
    void ballWithoutPlayerAfterKickOffFails() {
        Team home = genTeam();
        Team away = genTeam();
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
                        .addedTime(before.getAddedTime())
                        .home(before.getHome())
                        .away(before.getAway())
                        .ballState((new BallState(null, PitchArea.MIDFIELD)))
                        .plays(before.getPlays())
                        .build())
                .orElse(state));
    }
}