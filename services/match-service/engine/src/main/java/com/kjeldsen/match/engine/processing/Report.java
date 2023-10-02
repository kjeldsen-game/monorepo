package com.kjeldsen.match.engine.processing;

import static com.kjeldsen.match.domain.type.DuelResult.LOSE;
import static com.kjeldsen.match.domain.type.DuelResult.WIN;

import com.kjeldsen.match.domain.aggregate.Play;
import com.kjeldsen.match.domain.id.TeamId;
import com.kjeldsen.match.domain.type.DuelType;
import com.kjeldsen.match.domain.type.MatchResult;
import com.kjeldsen.match.engine.state.GameState;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;

@Slf4j
@Value
public class Report {

    /*
     * Generates post-match reports in the form of a narrated game possibly with additional
     * information such as rating changes and statistics.
     *
     * Other formats could be made available here.
     */

    // This is a quick illustration of how a narrated game could be generated and how ratings could
    // be included in the report. It just logs the results instead of producing a real report.
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

        log.info("#################### Match report ####################");

        for (Play play : endState.getPlays()) {
            if (play.getDuel().getReceiver() != null) {
                log.info("{} ({}) attempts to {} to player {} ({}) at {} minutes, and {}",
                    play.getDuel().getInitiator().getName(),
                    play.getDuel().getInitiator().getPosition(),
                    play.getAction(),
                    play.getDuel().getReceiver().getName(),
                    play.getDuel().getReceiver().getPosition(),
                    play.getMinute(),
                    play.getDuel().getResult() == WIN ? "succeeds" : "fails");
            } else if (play.getDuel().getType().equals(DuelType.SHOT)) {
                log.info("{} ({}) attempts to {} at {} minutes, and {}",
                    play.getDuel().getInitiator().getName(),
                    play.getDuel().getInitiator().getPosition(),
                    play.getAction(),
                    play.getMinute(),
                    play.getDuel().getResult() == WIN ? "scores" : "misses");
            } else {
                log.info("{} ({}) attempts to {} vs player {} ({}) at {} minutes, and {}",
                    play.getDuel().getInitiator().getName(),
                    play.getDuel().getInitiator().getPosition(),
                    play.getAction(),
                    play.getDuel().getChallenger().getName(),
                    play.getDuel().getChallenger().getPosition(),
                    play.getMinute(),
                    play.getDuel().getResult() == WIN ? "succeeds" : "fails");
            }
        }

        log.info("#################### Skill and ratings updates ####################");

        ImmutablePair<Integer, Integer> ratings = Ratings.eloRatings(homeScore, awayScore, result);
        log.info("Ratings before: home {} - away {}",
            endState.getHome().getScore(),
            endState.getAway().getScore());
        log.info("Ratings after: home {} - away {}", ratings.left, ratings.right);
    }
}
