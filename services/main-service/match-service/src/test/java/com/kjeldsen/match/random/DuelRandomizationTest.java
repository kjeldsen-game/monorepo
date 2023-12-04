package com.kjeldsen.match.random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kjeldsen.match.engine.RandomHelper;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.match.entities.Action;
import com.kjeldsen.match.entities.Play;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.entities.duel.Duel;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class DuelRandomizationTest {

    @Test
    void previousDuelFound() {
        Team home = RandomHelper.genTeam();
        Team away = RandomHelper.genTeam();
        Player initiator = RandomHelper.genPlayer(home);
        Player challenger = RandomHelper.genPlayer(away);
        Play first = Play.builder()
            .action(Action.PASS)
            .duel(Duel.builder()
                .type(DuelType.PASSING)
                .initiator(initiator)
                .challenger(challenger)
                .build())
            .clock(1)
            .build();
        Play second = Play.builder()
            .action(Action.POSITION)
            .duel(Duel.builder()
                .type(DuelType.POSITIONAL)
                .initiator(initiator)
                .challenger(challenger)
                .build())
            .clock(2)
            .build();

        GameState state = GameState.builder().plays(List.of(first, second)).build();

        Optional<Duel> previous =
            DuelRandomization.previousDuel(state, initiator, PlayerSkill.PASSING);
        assertTrue(previous.isPresent());
        assertEquals(first.getDuel(), previous.get());
    }
}