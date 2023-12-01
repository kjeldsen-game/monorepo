package com.kjeldsen.match.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kjeldsen.match.engine.entities.MatchResult;
import com.kjeldsen.match.engine.processing.Ratings;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.models.snapshot.PlaySnapshot;
import com.kjeldsen.match.models.snapshot.TeamSnapshot;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

@Builder
@Data
@Entity
@Table(name = "match_report")
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    TeamSnapshot home;
    @OneToOne
    TeamSnapshot away;

    @OneToMany
    List<PlaySnapshot> plays;

    Integer homeScore;
    Integer awayScore;

    @JsonIgnore
    @Transient
    GameStats gameStats; // Illustration of how to add additional information to the report

    public MatchReport(
        GameState state, List<PlaySnapshot> plays, TeamSnapshot home, TeamSnapshot away) {

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

        // This will be saved in a database
        this.gameStats = new GameStats(ratings.getLeft(), ratings.getRight());
    }

    public record GameStats(int homeRating, int awayRating) {

    }
}
