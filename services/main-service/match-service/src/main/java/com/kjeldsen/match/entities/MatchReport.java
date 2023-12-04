package com.kjeldsen.match.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kjeldsen.match.processing.Ratings;
import com.kjeldsen.match.state.GameState;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

@Builder
@Data
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class MatchReport {

    /*
     * Stores details about matches including all attributes and statistics of teams and players at
     * the time the match was played. These 'snapshots' contain information that may change across
     * matches.
     *
     * Used by frontends to display match results.
     */

    Long id;

    Team home;
    Team away;

    List<Play> plays;

    Integer homeScore;
    Integer awayScore;

    GameStats gameStats; // Illustration of how to add additional information to the report

    public MatchReport(
        GameState state, List<Play> plays, Team home, Team away) {

        this.home = home;
        this.away = away;
        this.plays = plays;
        this.homeScore = state.getHome().getScore();
        this.awayScore = state.getAway().getScore();

        MatchResult result;
        if (homeScore > awayScore) {
            result = MatchResult.WIN;
        } else if (awayScore > homeScore) {
            result = MatchResult.LOSS;
        } else {
            result = MatchResult.DRAW;
        }
        Pair<Integer, Integer> ratings = Ratings.eloRatings(homeScore, awayScore, result);

        this.gameStats = new GameStats(ratings.getLeft(), ratings.getRight());
    }

    public record GameStats(int homeRating, int awayRating) {

    }
}
