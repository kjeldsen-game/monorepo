package com.kjeldsen.match.engine;

import com.kjeldsen.match.domain.Game;
import com.kjeldsen.match.common.RandomHelper;
import com.kjeldsen.match.domain.entities.Action;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Play;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.entities.duel.DuelOrigin;
import com.kjeldsen.match.domain.entities.duel.DuelResult;
import com.kjeldsen.match.domain.entities.duel.DuelType;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerOrder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@RequiredArgsConstructor
class PlayerOrdersTest {

    Team home;
    Team away;
    Match match;

    @BeforeEach
    void setUp() {
        home = RandomHelper.genTeam(TeamRole.HOME);
        away = RandomHelper.genTeam(TeamRole.AWAY);
        match = Match.builder()
            .id(java.util.UUID.randomUUID().toString())
            .home(home)
            .away(away)
            .build();
    }

    @Test
    @Disabled
    void testLongShotConsistency() {

        // Build a home and away team with at least one player with intended player order.
        final PlayerOrder PLAYER_ORDER_TO_TEST = PlayerOrder.LONG_SHOT;
        while (home.getPlayers().stream().noneMatch(p -> p.getPosition().isMidfielder() && p.getPlayerOrder().equals(PLAYER_ORDER_TO_TEST))) {
            home = RandomHelper.genTeam(TeamRole.HOME);
        }
        while (away.getPlayers().stream().noneMatch(p -> p.getPosition().isMidfielder() && p.getPlayerOrder().equals(PLAYER_ORDER_TO_TEST))) {
            away = RandomHelper.genTeam(TeamRole.AWAY);
        }

        GameState state = Game.init(match);
        state = Game.kickOff(state);

        final int MATCH_PLAYS = 10000;
        final int PLAYER_ORDER_MAX_EXECUTIONS = 5;

        int playCounter = 0;
        int testPlayerOrdersExecuted = 0;
        while (testPlayerOrdersExecuted < PLAYER_ORDER_MAX_EXECUTIONS && playCounter < MATCH_PLAYS) {
            playCounter++;

            int homeScore = state.getHome().getScore();
            int awayScore = state.getAway().getScore();

            state = Game.nextPlay(state);
            Play lastPlay = state.lastPlay().get();

            if (DuelType.LONG_SHOT.equals(lastPlay.getDuel().getType())) {

                testPlayerOrdersExecuted++;

                if (DuelResult.WIN.equals(lastPlay.getDuel().getResult())) {
                    assertTrue(((homeScore + 1 == state.getHome().getScore())  || (awayScore + 1 == state.getAway().getScore())));
                }

                assertTrue(DuelOrigin.PLAYER_ORDER.equals(lastPlay.getDuel().getOrigin()));

                assertTrue(List.of(PitchArea.LEFT_MIDFIELD, PitchArea.CENTRE_MIDFIELD, PitchArea.RIGHT_MIDFIELD).contains(
                        lastPlay.getDuel().getPitchArea()));

            }

            if (!DuelOrigin.PLAYER_ORDER.equals(lastPlay.getDuel().getOrigin())
                && PLAYER_ORDER_TO_TEST.equals(lastPlay.getDuel().getAppliedPlayerOrder())) {
                fail();
            }
        }
        assertTrue(playCounter < MATCH_PLAYS);

    }

    @Test
    @Disabled
    void testPassToAreaConsistency() {

        // Build a home and away team with at least one player with intended player order.
        final PlayerOrder PLAYER_ORDER_TO_TEST = PlayerOrder.PASS_TO_AREA;
        while (!home.getPlayers().stream().anyMatch(p -> p.getPosition().isMidfielder() && p.getPlayerOrder().equals(PLAYER_ORDER_TO_TEST))) {
            home = RandomHelper.genTeam(TeamRole.HOME);
        }
        while (!away.getPlayers().stream().anyMatch(p -> p.getPosition().isMidfielder() && p.getPlayerOrder().equals(PLAYER_ORDER_TO_TEST))) {
            away = RandomHelper.genTeam(TeamRole.AWAY);
        }

        GameState state = Game.init(match);
        state = Game.kickOff(state);

        final int MATCH_PLAYS = 10000;
        final int PLAYER_ORDER_MAX_EXECUTIONS = 5;

        int playCounter = 0;
        int testPlayerOrdersExecuted = 0;

        while (testPlayerOrdersExecuted < PLAYER_ORDER_MAX_EXECUTIONS && playCounter < MATCH_PLAYS) {
            playCounter++;
            state = Game.nextPlay(state);
            Play lastPlay = state.lastPlay().get();
            if (DuelOrigin.PLAYER_ORDER.equals(lastPlay.getDuel().getOrigin())
                    && PLAYER_ORDER_TO_TEST.equals(lastPlay.getDuel().getAppliedPlayerOrder())) {

                testPlayerOrdersExecuted++;

                // Pass to area only for midfield areas.
                assertTrue(List.of(PitchArea.LEFT_MIDFIELD, PitchArea.CENTRE_MIDFIELD, PitchArea.RIGHT_MIDFIELD).contains(
                        lastPlay.getDuel().getPitchArea()));
                // Player current area and destination area cannot be the same.
                assertTrue(!lastPlay.getDuel().getInitiator().getPlayerOrderDestinationPitchArea().equals(
                        lastPlay.getDuel().getPitchArea()));

            }

            // Pass to area destination should be to adjacent areas. Here we check if the destination area was effectively nearby.
            if (state.beforeLastPlay().isPresent()) {
                Play beforeLastPlay = state.beforeLastPlay().get();

                if (DuelOrigin.PLAYER_ORDER.equals(beforeLastPlay.getDuel().getOrigin())
                        && PLAYER_ORDER_TO_TEST.equals(beforeLastPlay.getDuel().getAppliedPlayerOrder())) {
                    System.out.println("Pass to area origin: " + beforeLastPlay.getDuel().getPitchArea());
                    System.out.println("Pass to area destination: " + lastPlay.getDuel().getPitchArea());
                    assertTrue(lastPlay.getDuel().getPitchArea().isNearby(beforeLastPlay.getDuel().getPitchArea()), "Pass to area is only valid to adyacent areas.");
                }
            }

        }

        assertTrue(playCounter < MATCH_PLAYS);
    }

    @Test
    @Disabled
    void testDribbleToAreaConsistency() {

        // Build a home and away team with at least one player with intended player order.
        final PlayerOrder PLAYER_ORDER_TO_TEST = PlayerOrder.DRIBBLE_TO_AREA;
        while (!home.getPlayers().stream().anyMatch(p -> p.getPosition().isMidfielder() && p.getPlayerOrder().equals(PLAYER_ORDER_TO_TEST))) {
            home = RandomHelper.genTeam(TeamRole.HOME);
        }
        while (!away.getPlayers().stream().anyMatch(p -> p.getPosition().isMidfielder() && p.getPlayerOrder().equals(PLAYER_ORDER_TO_TEST))) {
            away = RandomHelper.genTeam(TeamRole.AWAY);
        }

        GameState state = Game.init(match);
        state = Game.kickOff(state);

        final int MATCH_PLAYS = 10000;
        final int PLAYER_ORDER_MAX_EXECUTIONS = 5;

        int playCounter = 0;
        int testPlayerOrdersExecuted = 0;
        while (testPlayerOrdersExecuted < PLAYER_ORDER_MAX_EXECUTIONS && playCounter < MATCH_PLAYS) {
            playCounter++;
            state = Game.nextPlay(state);
            Play lastPlay = state.lastPlay().get();
            if (DuelOrigin.PLAYER_ORDER.equals(lastPlay.getDuel().getOrigin())
                    && PLAYER_ORDER_TO_TEST.equals(lastPlay.getDuel().getAppliedPlayerOrder())) {

                testPlayerOrdersExecuted++;
                // Pass to area only for midfield areas.
                assertTrue(List.of(PitchArea.LEFT_MIDFIELD, PitchArea.CENTRE_MIDFIELD, PitchArea.RIGHT_MIDFIELD).contains(
                        lastPlay.getDuel().getPitchArea()));
                // Player current area and destination area cannot be the same.
                assertTrue(!lastPlay.getDuel().getInitiator().getPlayerOrderDestinationPitchArea().equals(
                        lastPlay.getDuel().getPitchArea()));

            }

            // Dribble should only occur on player order.
            if (Action.DRIBBLE.equals(lastPlay.getAction())) {
                assertEquals(DuelOrigin.PLAYER_ORDER, lastPlay.getDuel().getOrigin(), "Dribble can only happen based on player order.");
            }
                // Dribble to area destination should be to adjacent areas. Here we check if the destination area was effectively nearby.
            if (state.beforeLastPlay().isPresent()) {
                Play beforeLastPlay = state.beforeLastPlay().get();

                if (DuelOrigin.PLAYER_ORDER.equals(beforeLastPlay.getDuel().getOrigin())
                        && PLAYER_ORDER_TO_TEST.equals(beforeLastPlay.getDuel().getAppliedPlayerOrder())) {
                    System.out.println("Dribble to area origin: " + beforeLastPlay.getDuel().getPitchArea());
                    System.out.println("Dribble to area destination: " + lastPlay.getDuel().getPitchArea());
                    assertTrue(lastPlay.getDuel().getPitchArea().isNearby(beforeLastPlay.getDuel().getPitchArea()), "Dribble to area is only valid to adyacent areas.");
                }
            }

            assertTrue(playCounter < MATCH_PLAYS);
        }
    }

}