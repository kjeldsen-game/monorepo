package com.kjeldsen.match.engine;

import com.kjeldsen.match.Game;
import com.kjeldsen.match.common.RandomHelper;
import com.kjeldsen.match.entities.Action;
import com.kjeldsen.match.entities.DuelStats;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.Play;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.entities.TeamRole;
import com.kjeldsen.match.entities.duel.Duel;
import com.kjeldsen.match.entities.duel.DuelResult;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.match.recorder.GameProgressRecord;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class SkillsTest {

    Team home;
    Team away;
    Match match;

    @BeforeEach
    void setUp() {
/*
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.ERROR);
*/
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
    void testLowShotSkills() {
        GameState state = Game.init(match);
        state = Game.kickOff(state);

        int testDuelsExecuted = 0;
        int playCounter = 0;
        final int MATCH_PLAYS = 1000;
        while (testDuelsExecuted < 5 && playCounter < MATCH_PLAYS) {
            playCounter++;

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

                testDuelsExecuted++;
                System.out.println(testDuelsExecuted + " tested duels executed successfully.");
            }
        }
    }

    @Test
    @Disabled
    void testOneToOneShotSkills() {
        GameState state = Game.init(match);
        state = Game.kickOff(state);

        int testDuelsExecuted = 0;
        int playCounter = 0;
        final int MATCH_PLAYS = 1000;
        while (testDuelsExecuted < 5 && playCounter < MATCH_PLAYS) {
            playCounter++;
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

                testDuelsExecuted++;
                System.out.println(testDuelsExecuted + " tested duels executed successfully.");
            }
        }
    }

    @Test
    @Disabled
    void testHeaderShotSkills() {
        GameState state = Game.init(match);
        state = Game.kickOff(state);

        int testDuelsExecuted = 0;
        int playCounter = 0;
        final int MATCH_PLAYS = 1000;
        while (testDuelsExecuted < 5 && playCounter < MATCH_PLAYS) {
            playCounter++;
            state = Game.nextPlay(state);

            Play lastPlay = state.lastPlay().get();
            Duel lastDuel = lastPlay.getDuel();
            if (DuelType.HEADER_SHOT.equals(lastDuel.getType())) {
                int initiatorExpectedSkill = (lastDuel.getInitiator().getSkillValue(PlayerSkill.SCORING) + lastDuel.getInitiator().getSkillValue(
                    PlayerSkill.AERIAL)) / 2;
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

                testDuelsExecuted++;
                System.out.println(testDuelsExecuted + " tested duels executed successfully.");
            }
        }
    }

    @Test
    void testLongShotSkills() {
        GameState state = Game.init(match);
        state = Game.kickOff(state);

        int testDuelsExecuted = 0;
        int playCounter = 0;
        final int MATCH_PLAYS = 1000;
        while (testDuelsExecuted < 5 && playCounter < MATCH_PLAYS) {
            playCounter++;
            state = Game.nextPlay(state);

            Play lastPlay = state.lastPlay().get();
            Duel lastDuel = lastPlay.getDuel();
            if (DuelType.LONG_SHOT.equals(lastDuel.getType())) {
                int initiatorExpectedSkill = (lastDuel.getInitiator().getSkillValue(PlayerSkill.SCORING) + lastDuel.getInitiator().getSkillValue(
                    PlayerSkill.PASSING)) / 2;
                final int initiatorSkillLimit = lastDuel.getInitiator().getSkillValue(PlayerSkill.PASSING) + 10;
                if (initiatorExpectedSkill > initiatorSkillLimit) {
                    initiatorExpectedSkill = initiatorSkillLimit;
                }
                Integer challengedExpectedSkill = lastDuel.getChallenger().getSkillValue(PlayerSkill.REFLEXES);

                String detail = "GK reflexes:" + challengedExpectedSkill;
                state.getRecorder().record(detail, state, GameProgressRecord.Type.CALCULATION, GameProgressRecord.DuelStage.DURING);

                DuelStats initiatorDuelStats = lastDuel.getInitiatorStats();
                DuelStats challengerDuelStats = lastDuel.getChallengerStats();
/*
                assertEquals(initiatorExpectedSkill, initiatorDuelStats.getSkillPoints());
                assertEquals(challengedExpectedSkill, challengerDuelStats.getSkillPoints());
                assertTrue(List.of(PitchArea.LEFT_MIDFIELD, PitchArea.CENTRE_MIDFIELD, PitchArea.RIGHT_MIDFIELD).contains(lastPlay.getDuel()
                .getPitchArea()));
*/
                testDuelsExecuted++;
                System.out.println(testDuelsExecuted + " tested duels executed successfully.");
            }
        }
    }

    @Test
    @Disabled
    void testGoalkeeperPositioningSkill() {

        Player homeGoalkeeper = match.getHome().getPlayers(PlayerPosition.GOALKEEPER).get(0);
        homeGoalkeeper.setSkillValue(PlayerSkill.GOALKEEPER_POSITIONING, 100);

        GameState state = Game.init(match);
        state = Game.kickOff(state);

        int testDuelsExecuted = 0;
        int playCounter = 0;
        final int MATCH_PLAYS = 1000;
        while (testDuelsExecuted < 5 && playCounter < MATCH_PLAYS) {
            playCounter++;
            state = Game.nextPlay(state);

            Play lastPlay = state.lastPlay().get();
            Duel lastDuel = lastPlay.getDuel();

            if (PitchArea.CENTRE_FORWARD.equals(lastDuel.getPitchArea())
                && TeamRole.AWAY.equals(lastDuel.getInitiator().getTeamRole())
                && DuelType.POSITIONAL.equals(lastDuel.getType())
                && DuelResult.WIN.equals(lastDuel.getResult())) {

                //System.out.println("Possible shot in next action.");

            }

            if (Action.SHOOT.equals(lastPlay.getAction()) && TeamRole.AWAY.equals(lastDuel.getInitiator().getTeamRole())) {

                // Check bonus existed.
                int initiatorExpectedSkill = lastDuel.getInitiator().getSkillValue(PlayerSkill.SCORING);
                int challengedExpectedSkill = lastDuel.getChallenger().getSkillValue(PlayerSkill.INTERCEPTIONS);

                testDuelsExecuted++;
                //System.out.println(testDuelsExecuted + " test duels executed successfully.");
            }
        }
    }

    @Test
    @Disabled
    void testGoalkeeperInterceptionTendency() {

        Player homeGoalkeeper = match.getHome().getPlayers(PlayerPosition.GOALKEEPER).get(0);
        homeGoalkeeper.setSkillValue(PlayerSkill.INTERCEPTING, 100);

        GameState state = Game.init(match);
        state = Game.kickOff(state);

        int testDuelsExecuted = 0;
        int playCounter = 0;
        final int MATCH_PLAYS = 1000;
        while (testDuelsExecuted < 5 && playCounter < MATCH_PLAYS) {
            playCounter++;
            state = Game.nextPlay(state);

            Play lastPlay = state.lastPlay().get();
            Duel lastDuel = lastPlay.getDuel();

            if (PitchArea.CENTRE_FORWARD.equals(lastDuel.getPitchArea()) && DuelType.PASSING_HIGH.equals(lastDuel.getType())) {

                // Check bonus existed.
                int initiatorExpectedSkill = lastDuel.getInitiator().getSkillValue(PlayerSkill.SCORING);
                int challengedExpectedSkill = lastDuel.getChallenger().getSkillValue(PlayerSkill.INTERCEPTING);

                testDuelsExecuted++;
                //System.out.println(testDuelsExecuted + " test duels executed successfully.");
            }
        }
    }

}