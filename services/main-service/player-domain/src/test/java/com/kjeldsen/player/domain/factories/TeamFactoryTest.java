package com.kjeldsen.player.domain.factories;

import com.kjeldsen.player.domain.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class TeamFactoryTest {


    @Test
    @DisplayName("Should create a Team object")
    void should_create_team_object() {
        String userId = "user123";
        String teamName = "My Team";
        String teamId = "team123";

        Team team = TeamFactory.create(userId, teamName, teamId);

        assertThat(team).isNotNull();
        assertThat(team.getId()).isNotNull();
        assertThat(team.getUserId()).isEqualTo(userId);
        assertThat(team.getName()).isEqualTo(teamName);

        assertThat(team.getEconomy()).isNotNull();
        assertThat(team.getEconomy().getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1_000_000));
        assertThat(team.getEconomy().getPrices()).containsEntry(Team.Economy.PricingType.SEASON_TICKET, 14);
        assertThat(team.getEconomy().getSponsors()).containsKeys(
            Team.Economy.IncomePeriodicity.WEEKLY,
            Team.Economy.IncomePeriodicity.ANNUAL
        );

        assertThat(team.getBuildings()).isNotNull();
        assertThat(team.getBuildings().getFreeSlots()).isEqualTo(25);
        assertThat(team.getBuildings().getStadium()).isNotNull();
        assertThat(team.getBuildings().getFacilities()).containsKeys(
            Team.Buildings.Facility.TRAINING_CENTER,
            Team.Buildings.Facility.YOUTH_PITCH,
            Team.Buildings.Facility.SPORTS_DOCTORS,
            Team.Buildings.Facility.VIDEO_ROOM,
            Team.Buildings.Facility.SCOUTS
        );

        assertThat(team.getLeagueStats()).containsKey(1);
    }
}