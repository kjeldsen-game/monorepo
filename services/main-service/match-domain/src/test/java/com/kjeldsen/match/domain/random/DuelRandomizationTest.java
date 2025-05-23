package com.kjeldsen.match.domain.random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kjeldsen.match.common.RandomHelper;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.state.BallHeight;
import com.kjeldsen.match.domain.state.BallState;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.match.domain.entities.Action;
import com.kjeldsen.match.domain.entities.Play;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.entities.duel.Duel;
import com.kjeldsen.match.domain.entities.duel.DuelType;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerSkill;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class DuelRandomizationTest {

    @Test
    void previousDuelFound() {
        Team home = RandomHelper.genTeam(TeamRole.HOME);
        Team away = RandomHelper.genTeam(TeamRole.AWAY);
        Player initiator = RandomHelper.genPlayer(home);
        Player challenger = RandomHelper.genPlayer(away);
        BallState ballState = new BallState(initiator, PitchArea.CENTRE_MIDFIELD, BallHeight.GROUND);
        Play first = Play.builder()
            .action(Action.PASS)
            .duel(Duel.builder()
                .type(DuelType.PASSING_LOW)
                .initiator(initiator)
                .challenger(challenger)
                .build())
            .clock(1)
            .ballState(ballState)
            .build();
        Play second = Play.builder()
            .action(Action.POSITION)
            .duel(Duel.builder()
                .type(DuelType.POSITIONAL)
                .initiator(initiator)
                .challenger(challenger)
                .build())
            .clock(2)
            .ballState(ballState)
            .build();

        GameState state = GameState.builder().plays(List.of(first, second)).build();

        Optional<Duel> previous =
            DuelRandomization.previousDuel(state, initiator, List.of(PlayerSkill.PASSING), ballState);
        assertTrue(previous.isPresent());
        assertEquals(first.getDuel(), previous.get());
    }
}