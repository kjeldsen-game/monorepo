package com.kjeldsen.player.domain;

import com.kjeldsen.player.domain.events.CanteraInvestmentEvent;
import com.kjeldsen.player.domain.events.FansEvent;
import com.kjeldsen.player.domain.events.PricingEvent;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.util.*;

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
    private TeamModifiers teamModifiers;
    private Map<Integer, LeagueStats> leagueStats;
    private Buildings buildings;
    private String leagueId;


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

    public Integer getActualSeason() {
        try {
            return Collections.max(leagueStats.keySet());
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public Integer getActualSeasonTablePosition () {
        System.out.println(Collections.max(leagueStats.keySet()));
        try {
            return leagueStats.get(Collections.max(leagueStats.keySet())).getTablePosition();
        } catch (NullPointerException e) {
            return 12;
        }
    }

    public Integer getLastSeasonPosition() {
        Integer seasonNumber = Collections.max(leagueStats.keySet());
        try {
            return leagueStats.get(seasonNumber-1).getTablePosition();
        } catch (Exception e) {
            return  12;
        }
    }

    @Builder
    @Getter
    @Setter
    public static class Fans {
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

        public Integer getTotalFans() {
            return this.fanTiers.values().stream().mapToInt(FanTier::getTotalFans).sum();
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

        @Builder
        @Getter
        @Setter
        public static class FanTier {
            private Integer totalFans;
            private Double loyalty;
        }

        @Getter
        public enum LoyaltyImpactType {
            MATCH_DRAW,
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
                seats = 5000;
                this.setMaintenanceCost(BigDecimal.valueOf(seats));
            }

            @Override
            public void increaseLevel() {
                if (this.getSeats() == 100000) {
                    throw new RuntimeException("Stadium already have maximum level");
                }
                this.setLevel(this.getLevel() + 1);
                seats += 1000;
                // If the max seats are reached maintenance cost * 3
                setMaintenanceCost(getSeats() != 100000 ? BigDecimal.valueOf(seats) :
                    BigDecimal.valueOf(seats).multiply(BigDecimal.valueOf(3)));
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

        @Field(targetType = FieldType.DECIMAL128)
        private BigDecimal balance;
        private Map<PricingType, Integer> prices;
        private Map<IncomePeriodicity, IncomeMode> sponsors;
        private BillboardDeal billboardDeal;

        @Builder
        @Getter
        @Setter
        public static class BillboardDeal {
            private Integer startSeason;
            private Integer endSeason;
            private BillboardIncomeType type;
            private BigDecimal offer;
        }

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

        public void resetSponsorIncome(IncomePeriodicity periodicity) {
            this.getSponsors().put(periodicity, null);
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

        @Getter
        public enum BillboardIncomeType {
            SHORT(1),
            MEDIUM(2),
            LONG(3);

            private final int seasonLength;

            BillboardIncomeType(int length) {
                this.seasonLength = length;
            }
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
