package com.kjeldsen.match.engine;

import com.kjeldsen.match.engine.processing.Narration;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.models.Match;
import com.kjeldsen.match.models.MatchReport;
import com.kjeldsen.match.models.Player;
import com.kjeldsen.match.models.Team;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

class SimulationRunnerTest {

    @Test
    void runAndPrint() {
        Team home = TeamGenerator.randomTeam();
        Team away = TeamGenerator.randomTeam();

        Match match = Match.builder()
            .id(new Random().nextLong())
            .home(home)
            .away(away)
            .build();

        System.out.println("=".repeat(70));
        System.out.println("KJELDSEN MATCH SIMULATION");
        System.out.println("=".repeat(70));
        System.out.printf("HOME (ID %d) | Rating: %d | Tactic: %s | VP: %s | HP: %s%n",
            home.getId(), home.getRating(), home.getTactic(),
            home.getVerticalPressure(), home.getHorizontalPressure());
        System.out.printf("AWAY (ID %d) | Rating: %d | Tactic: %s | VP: %s | HP: %s%n",
            away.getId(), away.getRating(), away.getTactic(),
            away.getVerticalPressure(), away.getHorizontalPressure());
        System.out.println();
        System.out.println("HOME LINEUP:");
        for (Player p : home.getPlayers()) {
            System.out.printf("  %-30s [%s]%n", p.getName(), p.getPosition());
        }
        System.out.println();
        System.out.println("AWAY LINEUP:");
        for (Player p : away.getPlayers()) {
            System.out.printf("  %-30s [%s]%n", p.getName(), p.getPosition());
        }
        System.out.println("=".repeat(70));

        GameState state = Game.play(match);

        List<String> narration = Narration.narrate(state);
        int halfTimePrinted = 0;
        for (int i = 0; i < narration.size(); i++) {
            String play = narration.get(i);
            // Extract clock from the play text to detect half time boundary
            int clock = state.getPlays().get(i).getClock();
            if (clock > 45 && halfTimePrinted == 0) {
                System.out.println("-".repeat(70));
                System.out.println("HALF TIME");
                System.out.println("-".repeat(70));
                halfTimePrinted = 1;
            }
            System.out.println(play);
        }

        System.out.println("=".repeat(70));
        System.out.printf("FULL TIME: HOME %d - %d AWAY%n",
            state.getHome().getScore(), state.getAway().getScore());
        MatchReport report = new MatchReport(state, state.getPlays(), home, away);
        System.out.println("Attendance: " + report.getAttendance());
        System.out.println("Home ELO: " + report.getGameStats().homeRating());
        System.out.println("Away ELO: " + report.getGameStats().awayRating());
        System.out.println("Total plays: " + state.getPlays().size());
        System.out.println("=".repeat(70));
    }
}
