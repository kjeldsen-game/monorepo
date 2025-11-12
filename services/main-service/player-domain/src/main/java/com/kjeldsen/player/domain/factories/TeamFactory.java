package com.kjeldsen.player.domain.factories;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.TeamModifiers;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public final class TeamFactory {

    public static Team create(String userId, String teamName) {
        Team.TeamId newTeamId = Team.TeamId.generate();

        return Team.builder()
            .id(newTeamId)
            .userId(userId)
            .name(teamName)
            .cantera(Team.Cantera.builder()
                .score(0.0)
                .economyLevel(0)
                .traditionLevel(0)
                .buildingsLevel(0)
                .build())
            .teamModifiers(TeamModifiers.builder()
                .horizontalPressure(TeamModifiers.getRandomValueTeamModifier(TeamModifiers.HorizontalPressure.class))
                .verticalPressure(TeamModifiers.getRandomValueTeamModifier(TeamModifiers.VerticalPressure.class))
                .tactic(TeamModifiers.getRandomValueTeamModifier(TeamModifiers.Tactic.class))
                .build())
            .economy(Team.Economy.builder()
                .balance(BigDecimal.valueOf(1_000_000))
                .prices(new EnumMap<>(Map.of(
                    Team.Economy.PricingType.SEASON_TICKET, 14,
                    Team.Economy.PricingType.DAY_TICKET, 14,
                    Team.Economy.PricingType.MERCHANDISE, 25,
                    Team.Economy.PricingType.RESTAURANT, 10
                )))
                .sponsors(new HashMap<>() {{
                    put(Team.Economy.IncomePeriodicity.WEEKLY, null);
                    put(Team.Economy.IncomePeriodicity.ANNUAL, null);
                }})
                .billboardDeal(null)
                .build())
            .fans(Team.Fans.builder().build())
            .buildings(Team.Buildings.builder()
                .stadium(new Team.Buildings.Stadium())
                .freeSlots(25)
                .facilities(new EnumMap<>(Map.of(
                    Team.Buildings.Facility.TRAINING_CENTER, new Team.Buildings.FacilityData(),
                    Team.Buildings.Facility.YOUTH_PITCH, new Team.Buildings.FacilityData(),
                    Team.Buildings.Facility.SPORTS_DOCTORS, new Team.Buildings.FacilityData(),
                    Team.Buildings.Facility.VIDEO_ROOM, new Team.Buildings.FacilityData(),
                    Team.Buildings.Facility.SCOUTS, new Team.Buildings.FacilityData()
                )))
                .build())
            .leagueStats(new HashMap<>(Map.of(
                1, Team.LeagueStats.builder().tablePosition(12).points(0).build()
            )))
            .build();
    }
}