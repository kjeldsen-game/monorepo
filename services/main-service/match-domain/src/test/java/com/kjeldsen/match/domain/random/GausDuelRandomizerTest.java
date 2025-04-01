package com.kjeldsen.match.domain.random;

import com.kjeldsen.match.common.RandomHelper;
import com.kjeldsen.match.domain.entities.*;
import com.kjeldsen.match.domain.entities.duel.Duel;
import com.kjeldsen.match.domain.entities.duel.DuelRole;
import com.kjeldsen.match.domain.entities.duel.DuelType;
import com.kjeldsen.match.domain.state.BallHeight;
import com.kjeldsen.match.domain.state.BallState;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.player.domain.PitchArea;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Disabled
class GausDuelRandomizerTest {

    private static Player initiator;
    private static final List<Play> plays = new ArrayList<>();
    private static final GameState state = Mockito.mock(GameState.class);

    @BeforeAll
    static void setup() {
        Team home = RandomHelper.genTeam(TeamRole.HOME);
        Team away = RandomHelper.genTeam(TeamRole.AWAY);
        initiator = RandomHelper.genPlayer(home);
        Player challenger = RandomHelper.genPlayer(away);
        BallState ballState = new BallState(initiator, PitchArea.CENTRE_MIDFIELD, BallHeight.GROUND);
        plays.add(Play.builder()
            .action(Action.PASS)
            .duel(Duel.builder()
                .type(DuelType.PASSING_LOW)
                .initiator(initiator)
                .challenger(challenger)
                .build())
            .clock(1)
            .ballState(ballState)
            .build());
        plays.add(Play.builder()
            .action(Action.POSITION)
            .duel(Duel.builder()
                .type(DuelType.POSITIONAL)
                .initiator(initiator)
                .initiatorStats(DuelStats.builder().total(70).build())
                .challenger(challenger)
                .build())
            .clock(2)
            .ballState(ballState)
            .build());

        when(state.getPlays()).thenReturn(List.of(plays.get(0), plays.get(1)));
        when(state.getBallState()).thenReturn(ballState);
    }

    @Test
    void should_generate_random_gaus_with_previous_duel() {
        DuelStats.Performance performance = GausDuelRandomizer.performance(initiator,
            state,DuelType.POSITIONAL, DuelRole.INITIATOR);

        assertThat(performance.getRandom()).isBetween(-15.0, 15.0);
        assertEquals(initiator.duelSkill(DuelType.POSITIONAL, DuelRole.INITIATOR, state) - 70,
            performance.getPreviousTotalImpact());
        assertTrue(performance.getTotal() >= -15 && performance.getTotal() <= 15);

    }

    @Test
    void should_generate_gaus_random_without_previous_duel() {
        DuelStats.Performance performance = GausDuelRandomizer.performance(initiator,
            state,DuelType.LOW_SHOT, DuelRole.INITIATOR);

        assertThat(performance.getRandom()).isBetween(-15.0, 15.0);
        assertEquals(0, performance.getPreviousTotalImpact());
        assertTrue(performance.getTotal() >= -15 && performance.getTotal() <= 15);
    }
}