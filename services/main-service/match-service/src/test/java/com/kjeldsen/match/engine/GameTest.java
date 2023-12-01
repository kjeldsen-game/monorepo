package com.kjeldsen.match.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kjeldsen.match.engine.entities.Action;
import com.kjeldsen.match.engine.processing.ReportService;
import com.kjeldsen.match.models.MatchReport;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.models.Match;
import com.kjeldsen.match.models.Team;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class GameTest {

    private final ReportService reportService;


    @Test
    void kickOffIncrementsClock() {
        Team home = RandomHelper.genTeam();
        Team away = RandomHelper.genTeam();
        Match match = Match.builder()
            .home(home)
            .away(away)
            .build();

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
                .id(new Random().nextLong())
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
        Team home = RandomHelper.genTeam();
        Team away = RandomHelper.genTeam();
        Match match = Match.builder()
            .id(new Random().nextLong())
            .home(home)
            .away(away)
            .build();

        GameState state = Game.play(match);
        MatchReport report = reportService.generateReport(state, home, away);
    }

    @Test
    @Disabled
    void benchmark() {
        // Turn off logging when benchmarking
        long start = System.nanoTime();
        Team home = RandomHelper.genTeam();
        Team away = RandomHelper.genTeam();
        Match match = Match.builder()
            .id(new Random().nextLong())
            .home(home)
            .away(away)
            .build();

        int runs = 10;
        for (int i = 0; i < runs; i++) {
            Game.play(match);
        }
        long duration = System.nanoTime() - start;
        System.out.printf("Duration: %d ms%n", duration / 1000000);
    }
}