package com.kjeldsen.match.execution;

import com.kjeldsen.match.Game;
import com.kjeldsen.match.engine.RandomHelper;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.player.domain.PlayerStatus;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@RequiredArgsConstructor
class BenchTest {

    // Verify bench players are added to the game state.
    @Test
    void benchPlayersDoNotPlay() {
        Team home = RandomHelper.genTeam();
        Team away = RandomHelper.genTeam();
        Match match = Match.builder()
                .id(java.util.UUID.randomUUID().toString())
                .home(home)
                .away(away)
                .build();

        GameState state = Game.play(match);
        state.getPlays().forEach((play -> {
            assertNotEquals(play.getDuel().getInitiator().getStatus(), PlayerStatus.BENCH);
            if (play.getDuel().getChallenger() != null) {
                assertNotEquals(play.getDuel().getChallenger().getStatus(), PlayerStatus.BENCH);
            }
            if (play.getDuel().getType().requiresReceiver()) {
                assertNotEquals(play.getDuel().getReceiver().getStatus(), PlayerStatus.BENCH);
            }
        }));

        assertFalse(state.getHome().getBench().isEmpty());
        assertFalse(state.getAway().getBench().isEmpty());

    }

}