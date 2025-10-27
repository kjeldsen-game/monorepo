package com.kjeldsen.match.domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@Document(collection = "Leagues")
@TypeAlias("League")
public class League {

    LeagueId id;
    String name;
    Integer tier;
    Instant startedAt;
    Instant endedAt;
    Integer season;
    Boolean scheduledMatches;
    LeagueStatus status;
    Map<String, LeagueStats> teams;

    public void addTeam(String teamId, LeagueStats stats) {
        if (teams.size() >= 10) {
            throw new IllegalArgumentException("League is already full");
        }
        teams.put(teamId, stats);
    }

    public record LeagueId(String value) {

        public static LeagueId generate() {
            return new LeagueId(UUID.randomUUID().toString());
        }

        public static LeagueId of(String value) {
            return new LeagueId(value);
        }
    }

    public enum LeagueStatus {
        PRESEASON,
        ACTIVE,
        ENDED
    }

    @Getter
    @Setter
    public static class LeagueStats {
        String name;
        Integer position;
        Integer gamesPlayed = 0;
        Integer points = 0;
        Integer wins = 0;
        Integer losses = 0;
        Integer draws = 0;
        Integer goalsScored = 0;
        Integer goalsReceived = 0;
        boolean isBot;

        public void updateGoals(Integer scored, Integer received) {
            updateGoalsScored(scored);
            updateGoalsReceived(received);
        }

        private void updateGoalsScored(Integer goals) {
            this.goalsScored += goals;
        }

        private void updateGoalsReceived(Integer goals) {
            this.goalsReceived += goals;
        }

        public void handleWin() {
            this.wins++;
            this.points += 3;
            this.gamesPlayed++;
        }

        public void handleLoss() {
            this.losses++;
            this.gamesPlayed++;
        }

        public void handleDraw() {
            this.draws++;
            this.points += 1;
            this.gamesPlayed++;
        }
    }
}

