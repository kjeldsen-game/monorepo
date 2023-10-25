package com.kjeldsen.match.engine.processing;

import com.kjeldsen.match.entities.MatchResult;
import com.kjeldsen.match.engine.state.GameState;
import java.util.List;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Value
public class Report {

    /*
     * Generates post-match reports in the form of a narrated game possibly with additional
     * information such as rating changes and statistics.
     *
     * TODO - return JSON object for frontend to use
     */


    // Ignore - this will be moved to frontend instead of printing to the console
    public static void generate(GameState endState) {
        int homeScore = endState.getHome().getScore();
        int awayScore = endState.getAway().getScore();

        MatchResult result;
        if (homeScore > awayScore) {
            result = MatchResult.WIN;
        } else if (awayScore > homeScore) {
            result = MatchResult.LOSS;
        } else {
            result = MatchResult.DRAW;
        }

        System.out.println("#################### Match report ####################");

        List<String> narrated = Narration.narrate(endState);
        narrated.forEach(System.out::println);

        System.out.println("#################### Skill and ratings updates ####################");

        Pair<Integer, Integer> ratings = Ratings.eloRatings(homeScore, awayScore, result);
        log.info("Ratings before: home {} - away {}",
            endState.getHome().getScore(),
            endState.getAway().getScore());
        log.info("Ratings after: home {} - away {}", ratings.left(), ratings.right());
    }
}
