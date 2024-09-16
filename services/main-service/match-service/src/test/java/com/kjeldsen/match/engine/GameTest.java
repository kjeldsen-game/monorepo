package com.kjeldsen.match.engine;

import com.kjeldsen.match.Game;
import com.kjeldsen.match.common.RandomHelper;
import com.kjeldsen.match.entities.Action;
import com.kjeldsen.match.entities.DuelStats;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.MatchReport;
import com.kjeldsen.match.entities.Play;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.entities.duel.Duel;
import com.kjeldsen.match.entities.duel.DuelOrigin;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerOrder;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@RequiredArgsConstructor
class GameTest {

    Team home;
    Team away;
    Match match;

    @BeforeEach
    void setUp() {

        home = RandomHelper.genTeam();
        away = RandomHelper.genTeam();
        match = Match.builder()
                .id(java.util.UUID.randomUUID().toString())
                .home(home)
                .away(away)
                .build();

    }

    @Test
    void kickOffIncrementsClock() {
        GameState state = GameState.init(match);
        state = Game.kickOff(state);
        assertEquals(Action.PASS.getDuration(), state.getClock());
    }

    @Test
    @Disabled
    void viewAverageGoalsScored() {
        int homeGoals = 0;
        int awayGoals = 0;
        int trials = 1; // Increase o get a better average
        for (int i = 0; i < trials; i++) {
            Team home = RandomHelper.genTeam();
            Team away = RandomHelper.genTeam();
            Match match = Match.builder()
                .id(java.util.UUID.randomUUID().toString())
                .home(home)
                .away(away)
                .build();

            GameState state = Game.play(match);
            homeGoals += state.getHome().getScore();
            awayGoals += state.getAway().getScore();
        }
        System.out.printf("Home team average goals/game: %d%n", homeGoals / trials);
        System.out.printf("Away team average goals/game: %d%n", awayGoals / trials);
    }

    @Test
    @Disabled
    void viewGameNarration() {
        GameState state = Game.play(match);
        MatchReport report = new MatchReport(state, state.getPlays(), home, away);
    }

    @Test
    @Disabled
    void benchmark() {
        // Turn off logging when benchmarking
        long start = System.nanoTime();
        int runs = 100;
        for (int i = 0; i < runs; i++) {
            Game.play(match);
        }
        long duration = System.nanoTime() - start;
        System.out.printf("Duration: %d ms%n", duration / 1000000);
    }

    @Test
    @Disabled
    void playSequence() {
        GameState state = Game.init(match);
        state = Game.kickOff(state);

        final int MATCH_DURATION_IN_MINUTES = 90;
        for (int i = 1; i < MATCH_DURATION_IN_MINUTES; i++) {
            state = Game.nextPlay(state);
        }

        System.out.println("Game finished: [" + state.getHome().getScore() + "-" + state.getAway().getScore() + "]");

    }

    @Test
    @Disabled
    void printMatchBallHight() {
        GameState state = Game.init(match);
        state = Game.kickOff(state);

        final int MATCH_DURATION_IN_MINUTES = 90;
        for (int i = 0; i < MATCH_DURATION_IN_MINUTES; i++) {
            state = Game.nextPlay(state);
        }

        List<Play> plays = state.getPlays();

        System.out.println("");
        plays.stream().forEach(p -> { System.out.print(p.getBallState().getHeight().isHigh() ? "-" : "_");});
        System.out.println("");

        System.out.println("Game finished: [" + state.getHome().getScore() + "-" + state.getAway().getScore() + "]");
        System.out.println("");

    }

    @Test
    @Disabled
    void testLongShotConsistency() {
        GameState state = Game.init(match);
        state = Game.kickOff(state);

        final int MATCH_DURATION_IN_MINUTES = 90;
        for (int i = 0; i < MATCH_DURATION_IN_MINUTES; i++) {
            state = Game.nextPlay(state);

            Play lastPlay = state.lastPlay().get();

            if (DuelOrigin.PLAYER_ORDER.equals(lastPlay.getDuel().getOrigin())
                    && !PlayerOrder.LONG_SHOT.equals(lastPlay.getDuel().getAppliedPlayerOrder())) {
                if (Action.SHOOT.equals(lastPlay.getAction())) {
                    // If applied player order on Shoot action, then the Long shot didnt trigger.
                    fail();
                }
            }

            if (!DuelOrigin.PLAYER_ORDER.equals(lastPlay.getDuel().getOrigin())
                    && PlayerOrder.LONG_SHOT.equals(lastPlay.getDuel().getAppliedPlayerOrder())) {
                fail();
            }

            if (DuelOrigin.PLAYER_ORDER.equals(lastPlay.getDuel().getOrigin())
                    && PlayerOrder.LONG_SHOT.equals(lastPlay.getDuel().getAppliedPlayerOrder())) {
                assertTrue(List.of(PitchArea.LEFT_MIDFIELD, PitchArea.CENTRE_MIDFIELD, PitchArea.RIGHT_MIDFIELD).contains(lastPlay.getDuel().getPitchArea()));
            }

            if (DuelType.LONG_SHOT.equals(lastPlay.getDuel().getType())) {
                assertTrue(DuelOrigin.PLAYER_ORDER.equals(lastPlay.getDuel().getOrigin()));
            }

        }
    }

    @Test
    @Disabled
    void testSkillsLowShot() {
        GameState state = Game.init(match);
        state = Game.kickOff(state);

        int testedDuelsExecuted = 0;
        while (testedDuelsExecuted < 5) {
            state = Game.nextPlay(state);

            Play lastPlay = state.lastPlay().get();
            Duel lastDuel = lastPlay.getDuel();
            if (DuelType.LOW_SHOT.equals(lastDuel.getType())) {
                int initiatorExpectedSkill = lastDuel.getInitiator().getSkillValue(PlayerSkill.SCORING);
                int challengedExpectedSkill = lastDuel.getChallenger().getSkillValue(PlayerSkill.REFLEXES);

                DuelStats initiatorDuelStats = lastDuel.getInitiatorStats();
                DuelStats challengerDuelStats = lastDuel.getChallengerStats();

                assertEquals(initiatorExpectedSkill, initiatorDuelStats.getSkillPoints());
                assertEquals(challengedExpectedSkill, challengerDuelStats.getSkillPoints());
                assertTrue(List.of(PitchArea.CENTRE_FORWARD).contains(lastPlay.getDuel().getPitchArea()));

                testedDuelsExecuted++;
                System.out.println(testedDuelsExecuted + " tested duels executed successfully.");
            }
        }
    }

    @Test
    void testSkillsOneToOneShot() {
        GameState state = Game.init(match);
        state = Game.kickOff(state);

        int testedDuelsExecuted = 0;
        while (testedDuelsExecuted < 5) {
            state = Game.nextPlay(state);

            Play lastPlay = state.lastPlay().get();
            Duel lastDuel = lastPlay.getDuel();
            if (DuelType.ONE_TO_ONE_SHOT.equals(lastDuel.getType())) {
                int initiatorExpectedSkill = lastDuel.getInitiator().getSkillValue(PlayerSkill.SCORING);
                int challengedExpectedSkill = lastDuel.getChallenger().getSkillValue(PlayerSkill.ONE_ON_ONE);

                DuelStats initiatorDuelStats = lastDuel.getInitiatorStats();
                DuelStats challengerDuelStats = lastDuel.getChallengerStats();

                assertEquals(initiatorExpectedSkill, initiatorDuelStats.getSkillPoints());
                assertEquals(challengedExpectedSkill, challengerDuelStats.getSkillPoints());
                assertTrue(List.of(PitchArea.CENTRE_FORWARD).contains(lastPlay.getDuel().getPitchArea()));

                testedDuelsExecuted++;
                System.out.println(testedDuelsExecuted + " tested duels executed successfully.");
            }
        }
    }

    @Test
    void testSkillsHeaderShot() {
        GameState state = Game.init(match);
        state = Game.kickOff(state);

        int testedDuelsExecuted = 0;
        while (testedDuelsExecuted < 5) {
            state = Game.nextPlay(state);

            Play lastPlay = state.lastPlay().get();
            Duel lastDuel = lastPlay.getDuel();
            if (DuelType.HEADER_SHOT.equals(lastDuel.getType())) {
                int initiatorExpectedSkill = (lastDuel.getInitiator().getSkillValue(PlayerSkill.SCORING) + lastDuel.getInitiator().getSkillValue(PlayerSkill.AERIAL)) / 2;
                final int initiatorSkillLimit = lastDuel.getInitiator().getSkillValue(PlayerSkill.AERIAL) + 10;
                if (initiatorExpectedSkill > initiatorSkillLimit) {
                    initiatorExpectedSkill = initiatorSkillLimit;
                }
                Integer challengedExpectedSkill = lastDuel.getChallenger().getSkillValue(PlayerSkill.REFLEXES);

                DuelStats initiatorDuelStats = lastDuel.getInitiatorStats();
                DuelStats challengerDuelStats = lastDuel.getChallengerStats();

                assertEquals(initiatorExpectedSkill, initiatorDuelStats.getSkillPoints());
                assertEquals(challengedExpectedSkill, challengerDuelStats.getSkillPoints());
                assertTrue(List.of(PitchArea.CENTRE_FORWARD).contains(lastPlay.getDuel().getPitchArea()));

                testedDuelsExecuted++;
                System.out.println(testedDuelsExecuted + " tested duels executed successfully.");
            }
        }
    }

    @Test
    void testSkillsLongShot() {
        GameState state = Game.init(match);
        state = Game.kickOff(state);

        int testedDuelsExecuted = 0;
        while (testedDuelsExecuted < 5) {
            state = Game.nextPlay(state);

            Play lastPlay = state.lastPlay().get();
            Duel lastDuel = lastPlay.getDuel();
            if (DuelType.LONG_SHOT.equals(lastDuel.getType())) {
                int initiatorExpectedSkill = (lastDuel.getInitiator().getSkillValue(PlayerSkill.SCORING) + lastDuel.getInitiator().getSkillValue(PlayerSkill.PASSING)) / 2;
                final int initiatorSkillLimit = lastDuel.getInitiator().getSkillValue(PlayerSkill.PASSING) + 10;
                if (initiatorExpectedSkill > initiatorSkillLimit) {
                    initiatorExpectedSkill = initiatorSkillLimit;
                }
                Integer challengedExpectedSkill = lastDuel.getChallenger().getSkillValue(PlayerSkill.REFLEXES);

                DuelStats initiatorDuelStats = lastDuel.getInitiatorStats();
                DuelStats challengerDuelStats = lastDuel.getChallengerStats();

                assertEquals(initiatorExpectedSkill, initiatorDuelStats.getSkillPoints());
                assertEquals(challengedExpectedSkill, challengerDuelStats.getSkillPoints());
                assertTrue(List.of(PitchArea.LEFT_MIDFIELD, PitchArea.CENTRE_MIDFIELD, PitchArea.RIGHT_MIDFIELD).contains(lastPlay.getDuel().getPitchArea()));

                testedDuelsExecuted++;
                System.out.println(testedDuelsExecuted + " tested duels executed successfully.");
            }
        }
    }
}