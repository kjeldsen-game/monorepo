package com.kjeldsen.player.domain;

import com.kjeldsen.player.domain.events.CanteraInvestmentEvent;
import com.kjeldsen.player.domain.events.FansEvent;
import com.kjeldsen.player.domain.events.PricingEvent;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Document(collection = "Teams")
@TypeAlias("Team")
public class Team {

    private TeamId id;
    private String userId;
    private String name;
    private Economy economy;
    private Cantera cantera;
    private Fans fans;
    private Map<Integer, LeagueStats> leagueStats;
    private Buildings buildings;


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
    @Setter
    public static class LeagueStats {
        private Integer tablePosition;
        private Integer points;
        // TODO wins, losses, draw, cards, etc ....
    }

    @Builder
    @Getter
    @Setter
    public static class Fans {
        private Integer totalFans; // TODO remove
        private int loyalty;       // TODO remove

        @Builder.Default
        private Map<Integer, FanTier> fanTiers = new HashMap<>() {{
            put(1, FanTier.builder().totalFans(10000).loyalty(50.0).build());
        }};

        private void updateLoyalty(Integer tierId, Double adjustment) {
            fanTiers.computeIfPresent(tierId, (key, fanTier) -> {
                double newLoyalty = Math.max(0, Math.min(100, fanTier.getLoyalty() + adjustment));
                fanTier.setLoyalty(newLoyalty);
                return fanTier;
            });
        }

        public void updateAllLoyaltyTiers(List<Double> tiers) {
            for (Integer key : fanTiers.keySet()) {
                Double loyalty = tiers.get(key-1);
                updateLoyalty(key, loyalty);
            }
        }

        public void updateAllLoyaltyTiers(Double loyalty) {
            for (Integer key : fanTiers.keySet()) {
                updateLoyalty(key, loyalty);
            }
        }

        public void updateFans(FansEvent fansEvent) {
            updateFans(1, fansEvent.getFans());
        }

        public void updateFans(Integer tier, Integer fans) {
            fanTiers.compute(tier, (key, fanTier) -> {
                if (fanTier == null) {
                    return FanTier.builder().loyalty(50.0).totalFans(fans).build();
                }
                int updatedFans = fanTier.getTotalFans() + fans;
                if (updatedFans <= 0) {
                    return null;
                } else {
                    fanTier.setTotalFans(updatedFans);
                    return fanTier;
                }
            });
        }

        public void resetLoyalty() {
            this.fanTiers.forEach((tier, fanTier) -> {
                fanTier.setLoyalty(50.0);
            });
        }

        public void updateTotalFans(FansEvent fansEvent) { // TODO remove
            this.totalFans += fansEvent.getFans();
            if (totalFans < 0) {
                totalFans = 0;
            }
        }

        @Builder
        @Getter
        @Setter
        public static class FanTier {
            private Integer totalFans;
            private Double loyalty;
        }

        @Getter
        public enum LoyaltyImpactType {
            MATCH_WIN,
            MATCH_LOSS,
            SEASON_END,
            SEASON_START
        }

        @Getter
        public enum ImpactType {
            LOYALTY_TIER(null),
            MATCH_WIN(100),
            MATCH_LOSS(-100),
            MATCH_DRAW(50),
            SEASON_END(null);

            private final Integer fansImpact;

            ImpactType(Integer fansImpact) {
                this.fansImpact = fansImpact;
            }
        }
    }

    @Builder
    @Getter
    @Setter
    public static class Buildings {
        private Integer freeSlots;
        private Map<Facility, FacilityData> facilities;
        private Stadium stadium;

        @Getter
        public enum Facility {
            TRAINING_CENTER,
            YOUTH_PITCH,
            SPORTS_DOCTORS,
            VIDEO_ROOM,
            SCOUTS,
            STADIUM
        }

        @Getter
        @Setter
        public static class FacilityData {
            private Integer level;
            private BigDecimal maintenanceCost;

            public FacilityData() {
                this.level = 1;
                this.maintenanceCost = BigDecimal.valueOf(100_000);
            }

            public void increaseLevel() {
                level++;
                maintenanceCost = maintenanceCost.add(BigDecimal.valueOf(100_000));
            }

            public void decreaseLevel() throws IllegalArgumentException {
                if (this.level >= 2) {
                    level--;
                    maintenanceCost = maintenanceCost.subtract(BigDecimal.valueOf(100_000));
                } else {
                    throw new IllegalArgumentException("Cannot decrease level level to 1");
                }
            }
        }

        @Getter
        @Setter
        public static class Stadium extends FacilityData {
            private Integer seats;

            public Stadium() {
                seats = 10000;
                this.setMaintenanceCost(BigDecimal.valueOf(seats));
            }

            @Override
            public void increaseLevel() {
                this.setLevel(this.getLevel() + 1);
                seats += 1000;
                setMaintenanceCost(BigDecimal.valueOf(seats)); // Maintenance cost per seat is 1$
            }
        }


        public void updateFacility(Facility facility) throws  RuntimeException{
            if (facility.equals(Facility.STADIUM)) {
                getStadium().increaseLevel();
            } else {
                if (getFreeSlots() == 1) { // TODO add max level per
                    throw new RuntimeException("Cannot increase " + facility + " facility level, because there are no free slots");
                } else if (facilities.get(facility).getLevel() == 10) {
                    throw new RuntimeException("Cannot increase " + facility + " facility level, because it is on max level");
                } else {
                    facilities.get(facility).increaseLevel();
                    freeSlots -= 1;
                }
            }
        }
    }

    @Builder
    @Getter
    @Setter
    public static class Economy {

        private BigDecimal balance;
        private Map<PricingType, Integer> prices;
        private Map<IncomePeriodicity, IncomeMode> sponsors;
        private Map<IncomePeriodicity, IncomeMode> billboards;

        @Getter
        public enum PricingType {
            SEASON_TICKET(10,20),   //TODO adjust the season ticket prize
            DAY_TICKET(10,20),
            MERCHANDISE(10,25),
            RESTAURANT(4, 25);

            private final Integer minPrice;
            private final Integer maxPrice;

            PricingType(Integer minPrice, Integer maxPrice) {
                this.minPrice = minPrice;
                this.maxPrice = maxPrice;
            }
        }

        public void updatePrices(PricingEvent pricingEvent) {
            if (prices == null) {
                prices = new HashMap<>();
            }
            prices.put(pricingEvent.getPricingType(), pricingEvent.getPrice());
        }

        public void resetIncomeModes(IncomePeriodicity periodicity) {
            this.getSponsors().put(periodicity, null);
            this.getBillboards().put(periodicity, null);
        }

        public enum IncomePeriodicity {
            WEEKLY,
            ANNUAL,
        }

        public enum IncomeMode {
            CONSERVATIVE,
            MODERATE,
            AGGRESSIVE
        }

        public void updateBalance(BigDecimal amount) {
            balance = balance.add(amount);
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
            ECONOMY
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
