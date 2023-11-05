package com.kjeldsen.match.engine.random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kjeldsen.match.engine.RandomHelper;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.engine.entities.Action;
import com.kjeldsen.match.engine.entities.Play;
import com.kjeldsen.match.models.Team;
import com.kjeldsen.match.engine.entities.duel.Duel;
import com.kjeldsen.match.engine.entities.duel.DuelType;
import com.kjeldsen.match.models.Player;
import com.kjeldsen.match.engine.entities.SkillType;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.Test;

public class DuelRandomizationTest {

    @Test
    void previousDuelFound() {
        Team home = RandomHelper.genTeam();
        Team away = RandomHelper.genTeam();
        Player initiator = RandomHelper.genPlayer(home);
        Player challenger = RandomHelper.genPlayer(away);
        Play first = Play.builder()
            .id(new Random().nextLong())
            .action(Action.PASS)
            .duel(Duel.builder()
                .type(DuelType.PASSING)
                .initiator(initiator)
                .challenger(challenger)
                .build())
            .minute(1)
            .build();
        Play second = Play.builder()
            .id(new Random().nextLong())
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
            DuelRandomization.previousDuel(state, initiator, SkillType.PASSING);
        assertTrue(previous.isPresent());
        assertEquals(first.getDuel(), previous.get());
    }
}