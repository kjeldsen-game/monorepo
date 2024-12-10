package com.kjeldsen.match.engine;

import com.kjeldsen.match.Game;
import com.kjeldsen.match.common.RandomHelper;
import com.kjeldsen.match.entities.Action;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.MatchReport;
import com.kjeldsen.match.entities.Play;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.entities.TeamRole;
import com.kjeldsen.match.state.GameState;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
class GameTest {

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
            Team home = RandomHelper.genTeam(TeamRole.HOME);
            Team away = RandomHelper.genTeam(TeamRole.AWAY);
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
        MatchReport report = new MatchReport(state, state.getPlays(), home, away, 1000, 1000);
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
    void printMatchBallHeight() {
        GameState state = Game.init(match);
        state = Game.kickOff(state);

        final int MATCH_DURATION_IN_MINUTES = 90;
        for (int i = 0; i < MATCH_DURATION_IN_MINUTES; i++) {
            state = Game.nextPlay(state);
        }

        List<Play> plays = state.getPlays();

        System.out.println("");
        plays.stream().forEach(p -> {
            System.out.print(p.getBallState().getHeight().isHigh() ? "-" : "_");
        });
        System.out.println("");

        System.out.println("Game finished: [" + state.getHome().getScore() + "-" + state.getAway().getScore() + "]");
        System.out.println("");

    }

}