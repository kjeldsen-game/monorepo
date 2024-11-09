package com.kjeldsen.player.application.testdata;

import com.github.javafaker.Faker;
import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.provider.PlayerProvider;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

public class TestData {

    public static Team.TeamId generateTestTeamId() {
        return Team.TeamId.generate();
    }

    public static List<Player> generateTestPlayers(Team.TeamId teamId, int numberOfPlayers) {
        return IntStream.range(0, numberOfPlayers)
            .mapToObj(i -> {
                PlayerPositionTendency positionTendency = PlayerPositionTendency.getDefault(PlayerProvider.position());
                PlayerCategory playerCategory = i >= numberOfPlayers / 2 ? PlayerCategory.JUNIOR : PlayerCategory.SENIOR;
                return PlayerProvider.generate(teamId, positionTendency, playerCategory, 200);
            }).toList();
    }

    public static Team generateTestTeam(Team.TeamId teamId) {
        return Team.builder()
            .id(teamId)
            .userId(UUID.randomUUID().toString())
            .cantera(Team.Cantera.builder()
                .score(0.0)
                .economyLevel(0)
                .traditionLevel(0)
                .buildingsLevel(0)
                .build())
            .name(generateRandomTeamName())
            .economy(Team.Economy.builder()
                .balance(BigDecimal.ZERO)
                .prices(generatePricingMap())
                .sponsors(new HashMap<>() {{
                    put(Team.Economy.IncomePeriodicity.WEEKLY, null);
                    put(Team.Economy.IncomePeriodicity.ANNUAL, null);
                }})
                .build())
            .fans(Team.Fans.builder().build())
            .leagueStats(generateLeagueStatsMap())
            .buildings(Team.Buildings.builder()
                .stadium(new Team.Buildings.Stadium())
                .facilities(generateFacilitiesMap())
                .freeSlots(25)
                .build())
            .build();
    }

    private static String generateRandomTeamName() {
        Faker faker = new Faker();
        return faker.company().name() + " " + faker.color().name();
    }

    private static Map<Integer, Team.LeagueStats> generateLeagueStatsMap() {
        return new HashMap<>(Map.of(
            1, Team.LeagueStats.builder().points(10).tablePosition(2).build(),
            2, Team.LeagueStats.builder().points(10).tablePosition(5).build()));
    }

    private static Map<Team.Economy.PricingType, Integer> generatePricingMap() {
        return new HashMap<>(Map.of(
            Team.Economy.PricingType.SEASON_TICKET, 14,
            Team.Economy.PricingType.DAY_TICKET, 14,
            Team.Economy.PricingType.MERCHANDISE, 5,
            Team.Economy.PricingType.RESTAURANT, 10
        ));
    }

    private static Map<Team.Buildings.Facility , Team.Buildings.FacilityData> generateFacilitiesMap() {
        return new HashMap<>(Map.of(
                Team.Buildings.Facility.SCOUTS, new Team.Buildings.FacilityData()
        ));
    }
}
