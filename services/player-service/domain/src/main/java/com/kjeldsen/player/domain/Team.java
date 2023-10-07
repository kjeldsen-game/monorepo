package com.kjeldsen.player.domain;

import com.kjeldsen.player.domain.events.CanteraInvestmentEvent;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "Teams")
@TypeAlias("Team")
public class Team {

    private TeamId id;
    private String userId;
    private String name;
    private List<Player> players;
    private Cantera cantera;

    public record TeamId(String value) {
        public static TeamId generate() {
            return new TeamId(java.util.UUID.randomUUID().toString());
        }

        public static TeamId of(String id) {
            return new TeamId(id);
        }
    }

    @Builder
    @Getter
    public static class Cantera {

        public static final int MAX_INVESTMENT_LEVEL = 100;

        private Double score;
        private Integer economyLevel;
        private Integer traditionLevel;
        private Integer buildingsLevel;

        public enum Investment {
            TRADITION,
            BUILDINGS,
            ECONOMY;
        }

        public void addEconomyInvestment(CanteraInvestmentEvent canteraInvestmentEvent) {
            economyLevel = Math.min(economyLevel + canteraInvestmentEvent.getPoints(), MAX_INVESTMENT_LEVEL);
            recalculateScore();
        }

        public void addTraditionInvestment(CanteraInvestmentEvent canteraInvestmentEvent) {
            traditionLevel = Math.min(traditionLevel + canteraInvestmentEvent.getPoints(), MAX_INVESTMENT_LEVEL);
            recalculateScore();
        }

        public void addBuildingsInvestment(CanteraInvestmentEvent canteraInvestmentEvent) {
            buildingsLevel = Math.min(buildingsLevel + canteraInvestmentEvent.getPoints(), MAX_INVESTMENT_LEVEL);
            recalculateScore();
        }

        private void recalculateScore() {
            score = 0.0;
            score += economyLevel * 0.5;
            score += traditionLevel * 0.25;
            score += buildingsLevel * 0.25;
        }

    }

}
