package com.kjeldsen.match.engine;

import com.kjeldsen.match.Game;
import com.kjeldsen.match.common.RandomHelper;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.Play;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.entities.TeamRole;
import com.kjeldsen.match.entities.duel.DuelResult;
import com.kjeldsen.match.state.GameState;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class ChainedActionSequenceTest {

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
    void testChainedActionSequenceConsistency() {

        GameState state = Game.init(match);
        state = Game.kickOff(state);

        final int MATCH_PLAYS = 10000;

        int playCounter = 0;
        while (playCounter < MATCH_PLAYS) {
            playCounter++;
            state = Game.nextPlay(state);
            Play lastPlay = state.lastPlay().get();

            if (state.beforeLastPlay().isPresent()) {
                Play beforeLastPlay = state.beforeLastPlay().get();

                if (beforeLastPlay.getChainActionSequence().isActive()
                        && DuelResult.LOSE.equals(beforeLastPlay.getDuel().getResult())) {

                    // Chained actions sequence should end if last duel is lost.
                    assertFalse(lastPlay.getChainActionSequence().isActive());

                }
            }

            assertTrue(playCounter < MATCH_PLAYS);
        }
    }

}