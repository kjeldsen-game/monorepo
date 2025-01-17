package com.kjeldsen.match.execution;

import com.kjeldsen.match.Game;
import com.kjeldsen.match.common.RandomHelper;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.entities.TeamRole;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.player.domain.PlayerStatus;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@RequiredArgsConstructor
class BenchTest {

    // Verify bench players are added to the game state.
    @Test
    @Disabled
    void benchPlayersDoNotPlay() {
        Team home = RandomHelper.genTeam(TeamRole.HOME);
        Team away = RandomHelper.genTeam(TeamRole.AWAY);
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
            if (play.getAction().requiresReceiver()) {
                assertNotEquals(play.getDuel().getReceiver().getStatus(), PlayerStatus.BENCH);
            }
        }));

        assertFalse(state.getHome().getBench().isEmpty());
        assertFalse(state.getAway().getBench().isEmpty());

    }

}