package com.kjeldsen.match.engine.processing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kjeldsen.match.engine.entities.MatchResult;
import com.kjeldsen.match.engine.entities.Play;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.models.Team;
import java.util.List;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

@Slf4j
@Value
public class Report {

    /*
     * Generates post-match reports in the form of a narrated game possibly with additional
     * information such as rating changes and statistics. This report will be used by the frontend
     * to display the game.
     */

    Team home;
    Team away;
    List<Play> plays;
    int homeScore;
    int awayScore;
    @JsonIgnore
    List<String> narration;
    @JsonIgnore
    GameStats gameStats; // Illustration of how to add additional information to the report


    public Report(GameState endState, Team home, Team away) {
        this.home = home;
        this.away = away;

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
        List<String> narrated = Narration.narrate(endState);
        Pair<Integer, Integer> ratings = Ratings.eloRatings(homeScore, awayScore, result);

        this.plays = endState.getPlays();
        this.narration = narrated;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.gameStats = new GameStats(ratings.getLeft(), ratings.getRight());
    }

    public record GameStats(int homeRating, int awayRating) {

    }
}
