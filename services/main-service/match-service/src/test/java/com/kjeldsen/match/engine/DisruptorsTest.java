package com.kjeldsen.match.engine;

import com.kjeldsen.match.Game;
import com.kjeldsen.match.common.RandomHelper;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.Play;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.entities.TeamRole;
import com.kjeldsen.match.entities.duel.Duel;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class DisruptorsTest {

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