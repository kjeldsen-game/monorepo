package com.kjeldsen.match.execution;

import com.kjeldsen.match.Game;
import com.kjeldsen.match.common.RandomHelper;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.Play;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.entities.duel.Duel;
import com.kjeldsen.match.entities.duel.DuelOrigin;
import com.kjeldsen.match.entities.duel.DuelResult;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.match.random.DuelRandomization;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class DuelExecutionTest {

    @Test
    void goalkeeperCheckOnShotDuelResolution() {
        Team home = RandomHelper.genTeam();
        Team away = RandomHelper.genTeam();

        Match match = Match.builder()
                .id(java.util.UUID.randomUUID().toString())
                .home(home)
                .away(away)
                .build();
        GameState state = GameState.init(match);

        Player initiator = home.getPlayers().stream().filter(p -> p.getPosition().equals(PlayerPosition.FORWARD)).findAny().get();
        initiator.getSkills().put(PlayerSkill.SCORING, 0);
        Player challenger = away.getPlayers().stream().filter(p -> p.getPosition().equals(PlayerPosition.FORWARD)).findAny().get();
        challenger.getSkills().put(PlayerSkill.REFLEXES, 100);

        DuelParams params = DuelParams.builder()
                .state(state)
                .duelType(DuelType.LOW_SHOT)
                .initiator(initiator)
                .challenger(challenger)
                .origin(DuelOrigin.DEFAULT)
                .build();

        try {
            DuelDTO outcome = DuelExecution.executeDuel(state, params);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(),"Player defending shot is not a GOALKEEPER.");
        }
    }

        @Test
    void shotDuelResolutionWinAndLose() {
        Team home = RandomHelper.genTeam();
        Team away = RandomHelper.genTeam();

        Match match = Match.builder()
                .id(java.util.UUID.randomUUID().toString())
                .home(home)
                .away(away)
                .build();
        GameState state = GameState.init(match);

        Player initiator = home.getPlayers().stream().filter(p -> p.getPosition().equals(PlayerPosition.FORWARD)).findAny().get();
        initiator.getSkills().put(PlayerSkill.SCORING, 0);
        Player challenger = away.getPlayers().stream().filter(p -> p.getPosition().equals(PlayerPosition.GOALKEEPER)).findAny().get();
        challenger.getSkills().put(PlayerSkill.REFLEXES, 100);

        DuelParams params = DuelParams.builder()
                .state(state)
                .duelType(DuelType.LOW_SHOT)
                .initiator(initiator)
                .challenger(challenger)
                .origin(DuelOrigin.DEFAULT)
                .build();

        DuelDTO outcome = DuelExecution.executeDuel(state, params);
        assertEquals(outcome.getResult(), DuelResult.LOSE);

        initiator.getSkills().put(PlayerSkill.SCORING, 100);
        challenger.getSkills().put(PlayerSkill.REFLEXES, 0);
        params = DuelParams.builder()
                .state(state)
                .duelType(DuelType.LOW_SHOT)
                .initiator(initiator)
                .challenger(challenger)
                .origin(DuelOrigin.DEFAULT)
                .build();

        outcome = DuelExecution.executeDuel(state, params);
        assertEquals(outcome.getResult(), DuelResult.WIN);

    }

    @Test
    void aerialPassDuelResolution() {
        Team home = RandomHelper.genTeam();
        Team away = RandomHelper.genTeam();

        Match match = Match.builder()
                .id(java.util.UUID.randomUUID().toString())
                .home(home)
                .away(away)
                .build();
        GameState state = GameState.init(match);

        state = Game.kickOff(state);
        state = Game.nextPlay(state);

        Player initiator = home.getPlayers().stream().filter(p -> p.getPosition().equals(PlayerPosition.LEFT_MIDFIELDER) || p.getPosition().equals(PlayerPosition.LEFT_WINGER)) .findAny().get();
        initiator.getSkills().put(PlayerSkill.BALL_CONTROL, 0);
        initiator.getSkills().put(PlayerSkill.AERIAL, 100);
        Player challenger = away.getPlayers().stream().filter(p -> p.getPosition().equals(PlayerPosition.CENTRE_BACK)).findAny().get();
        challenger.getSkills().put(PlayerSkill.BALL_CONTROL, 0);
        challenger.getSkills().put(PlayerSkill.AERIAL, 0);

        DuelParams params = DuelParams.builder()
                .state(state)
                .duelType(DuelType.BALL_CONTROL)
                .initiator(initiator)
                .challenger(challenger)
                .origin(DuelOrigin.DEFAULT)
                .build();

        DuelDTO outcome = DuelExecution.executeDuel(state, params);
        assertEquals(outcome.getResult(), DuelResult.WIN);

    }

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
        Player initiator = Player.builder().skills(Map.of(PlayerSkill.OFFENSIVE_POSITIONING, 40))
            .build();
        Player challenger = Player.builder().skills(Map.of(PlayerSkill.DEFENSIVE_POSITIONING, 60))
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
                .clock(i)
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
        Player initiator = Player.builder().skills(Map.of(PlayerSkill.OFFENSIVE_POSITIONING, 40))
            .build();
        Player challenger = Player.builder().skills(Map.of(PlayerSkill.DEFENSIVE_POSITIONING, 60))
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
                .clock(i)
                .build();
            plays.add(build);
        }

        GameState state = GameState.builder().plays(plays).build();

        int consecutiveLosses =
            DuelExecution.consecutiveLosses(state, challenger, DuelType.BALL_CONTROL);

        assertEquals(4, consecutiveLosses);
    }
}