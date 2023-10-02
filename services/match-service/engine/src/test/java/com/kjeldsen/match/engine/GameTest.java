package com.kjeldsen.match.engine;

import static com.kjeldsen.match.engine.utils.RandomHelper.genTeam;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kjeldsen.match.domain.aggregate.Match;
import com.kjeldsen.match.domain.aggregate.Team;
import com.kjeldsen.match.domain.id.MatchId;
import com.kjeldsen.match.domain.type.Action;
import com.kjeldsen.match.engine.state.GameState;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import com.kjeldsen.match.engine.processing.Report;

class GameTest {

    @Test
    void kickOffIncrementsClock() {
        Team home = genTeam();
        Team away = genTeam();
        Match match = Match.builder()
            .home(home)
            .away(away)
            .build();

        GameState state = GameState.init(match);
        state = Game.kickOff(state);
        assertEquals(Action.PASS.getDuration(), state.getClock());
    }

    @Test
    void play() {
        Team home = genTeam();
        Team away = genTeam();
        Match match = Match.builder()
            .matchId(MatchId.generate())
            .home(home)
            .away(away)
            .build();

        GameState state = Game.play(match);
        Report.generate(state);
    }


    @Test
    @Disabled
    void benchmark() {
        // Turn off logging when benchmarking

        long start = System.nanoTime();
        Team home = genTeam();
        Team away = genTeam();
        Match match = Match.builder()
            .matchId(MatchId.generate())
            .home(home)
            .away(away)
            .build();

        int runs = 1;
        for (int i = 0; i < runs; i++) {
            Game.play(match);
        }

        long duration = System.nanoTime() - start;
        System.out.printf("Duration: %d ms%n", duration / 1000000);
    }
}