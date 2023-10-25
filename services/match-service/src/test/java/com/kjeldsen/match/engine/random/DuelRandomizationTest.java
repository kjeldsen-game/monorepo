package com.kjeldsen.match.engine.random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kjeldsen.match.engine.RandomHelper;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.entities.Action;
import com.kjeldsen.match.entities.Id;
import com.kjeldsen.match.entities.Play;
import com.kjeldsen.match.entities.duel.Duel;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.match.entities.player.Player;
import com.kjeldsen.match.entities.player.PlayerSkill;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class DuelRandomizationTest {

    @Test
    void previousDuelFound() {
        Player initiator = RandomHelper.genPlayer(Id.generate());
        Player challenger = RandomHelper.genPlayer(Id.generate());
        Play first = Play.builder()
            .id(Id.generate())
            .action(Action.PASS)
            .duel(Duel.builder()
                .type(DuelType.PASSING)
                .initiator(initiator)
                .challenger(challenger)
                .build())
            .minute(1)
            .build();
        Play second = Play.builder()
            .id(Id.generate())
            .action(Action.POSITION)
            .duel(Duel.builder()
                .type(DuelType.POSITIONAL)
                .initiator(initiator)
                .challenger(challenger)
                .build())
            .minute(2)
            .build();

        GameState state = GameState.builder().plays(List.of(first, second)).build();

        Optional<Duel> previous =
            DuelRandomization.previousDuel(state, initiator, PlayerSkill.PASSING);
        assertTrue(previous.isPresent());
        assertEquals(first.getDuel(), previous.get());
    }
}