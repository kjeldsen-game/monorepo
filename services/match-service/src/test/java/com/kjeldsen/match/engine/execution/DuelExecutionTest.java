package com.kjeldsen.match.engine.execution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kjeldsen.match.entities.Play;
import com.kjeldsen.match.entities.duel.Duel;
import com.kjeldsen.match.entities.duel.DuelResult;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.match.entities.player.Player;
import com.kjeldsen.match.entities.player.PlayerSkill;
import com.kjeldsen.match.engine.random.DuelRandomization;
import com.kjeldsen.match.engine.state.GameState;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class DuelExecutionTest {


    @Test
    @Disabled
    void viewWinDistribution() {
        int wins = 0;
        boolean lostLast = false;
        for (int i = 0; i < 100; i++) {
            double p = DuelRandomization.initialWinProbability(40, 60);
            double rand = new Random().nextDouble();
            if (lostLast) {
                rand += 0.05;
            }
            if (rand < p) {
                wins++;
            } else {
                lostLast = true;
            }
        }
        System.out.println(wins);
    }


    @Test
    void consecutiveLossesInitiator() {
        Player initiator = Player.builder().skillSet(Map.of(PlayerSkill.OFFENSIVE_POSITIONING, 40))
            .build();
        Player challenger = Player.builder().skillSet(Map.of(PlayerSkill.DEFENSIVE_POSITIONING, 60))
            .build();

        List<DuelResult> results = List.of(
            DuelResult.WIN,
            DuelResult.LOSE,
            DuelResult.LOSE,
            DuelResult.WIN,
            DuelResult.LOSE,
            DuelResult.LOSE,
            DuelResult.LOSE);

        List<Play> plays = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            DuelResult result = results.get(i);
            Duel duel = Duel.builder()
                .initiator(initiator)
                .challenger(challenger)
                .result(result)
                .type(DuelType.BALL_CONTROL)
                .build();
            Play build = Play.builder()
                .duel(duel)
                .minute(i)
                .build();
            plays.add(build);
        }

        GameState state = GameState.builder().plays(plays).build();

        int consecutiveLosses =
            DuelExecution.consecutiveLosses(state, initiator, DuelType.BALL_CONTROL);

        assertEquals(3, consecutiveLosses);
    }

    @Test
    void consecutiveLossesChallenger() {
        Player initiator = Player.builder().skillSet(Map.of(PlayerSkill.OFFENSIVE_POSITIONING, 40))
            .build();
        Player challenger = Player.builder().skillSet(Map.of(PlayerSkill.DEFENSIVE_POSITIONING, 60))
            .build();

        List<DuelResult> results = List.of(
            DuelResult.WIN,
            DuelResult.LOSE,
            DuelResult.WIN,
            DuelResult.LOSE,
            DuelResult.LOSE,
            DuelResult.LOSE,
            DuelResult.LOSE);

        List<Play> plays = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            DuelResult result = results.get(i);
            Duel duel = Duel.builder()
                .initiator(initiator)
                .challenger(challenger)
                .result(result)
                .type(DuelType.BALL_CONTROL)
                .build();
            Play build = Play.builder()
                .duel(duel)
                .minute(i)
                .build();
            plays.add(build);
        }

        GameState state = GameState.builder().plays(plays).build();

        int consecutiveLosses =
            DuelExecution.consecutiveLosses(state, challenger, DuelType.BALL_CONTROL);

        assertEquals(4, consecutiveLosses);
    }
}