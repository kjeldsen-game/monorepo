package com.kjeldsen.player.domain;

import com.kjeldsen.player.domain.events.FansEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.text.TabExpander;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TeamTest {

    @Test
    public void should_return_not_empty_or_not_null_teamId_value_on_generate_method() {
        Team.TeamId teamId = Team.TeamId.generate();
        Assertions.assertNotNull(teamId.value());
        Assertions.assertFalse(teamId.value().isEmpty());
    }

    @Test
    public void should_return_a_unique_teamId_value_on_generate_method() {
        Team.TeamId teamIdOne = Team.TeamId.generate();
        Team.TeamId teamIdTwo = Team.TeamId.generate();
        Assertions.assertNotEquals(teamIdOne.value(), teamIdTwo.value());
    }

    @Test
    public void should_assign_a_correct_value_on_teamIdOf_method() {
        String exampleTeamId = "exampleId";
        Team.TeamId teamId = Team.TeamId.of(exampleTeamId);
        Assertions.assertEquals(exampleTeamId, teamId.value());
    }



    // ********* Start LeagueStats Tests *********
    @Test
    public void should_return_minus_one_if_leagueStats_not_defined() {
        Team testTeam = Team.builder().leagueStats(null).build();
        assertEquals(-1, testTeam.getActualSeason());
    }
    @Test
    public void should_return_last_position_if_no_prev_position() {
        Team testTeam = Team.builder().leagueStats(Map.of(
            1, Team.LeagueStats.builder().points(12).tablePosition(1).build()
        )).build();
        assertEquals(12, testTeam.getLastSeasonPosition());
    }
    // ********* End LeagueStats Tests *********



    // ********* Start Fans Tests *********

    @Test
    public void should_return_total_fans() {
        Team team = Team.builder().fans(Team.Fans.builder().build()).build();
        // Value of fans when team is created is set to 10000
        assertThat(team.getFans().getTotalFans()).isEqualTo(10000);
    }

    @Test
    public void should_update_loyalty_double_input() {
        Team team = Team.builder().fans(Team.Fans.builder().build()).build();
        Double testLoyaltyValue = 1.0;
        team.getFans().updateAllLoyaltyTiers(testLoyaltyValue);
        assertThat(team.getFans().getFanTiers().values())
            .allSatisfy(fanTier -> assertThat(fanTier.getLoyalty()).isEqualTo(51.0));
    }

    @Test
    public void should_update_loyalty_list_input() {
        Team team = Team.builder().fans(Team.Fans.builder().build()).build();
        team.getFans().getFanTiers().put(2, Team.Fans.FanTier.builder().loyalty(50.0).build());
        List<Double> testLoyaltyList = List.of(1.0, 2.0, 3.0);
        team.getFans().updateAllLoyaltyTiers(testLoyaltyList);

        Assertions.assertEquals(51.0, team.getFans().getFanTiers().get(1).getLoyalty());
        Assertions.assertEquals(52.0, team.getFans().getFanTiers().get(2).getLoyalty());
    }

    @Test
    public void update_fans() {
        Team team = Team.builder().fans(Team.Fans.builder().build()).build();
        team.getFans().updateFans(1,10);
        assertThat(team.getFans().getFanTiers().get(1).getTotalFans()).isEqualTo(10010);
    }

    @Test
    public void update_fans_fansEvent_input() {
        Team team = Team.builder().fans(Team.Fans.builder().build()).build();
        FansEvent testFansEvent = FansEvent.builder().fans(10).build();
        team.getFans().updateFans(testFansEvent);
        assertThat(team.getFans().getFanTiers().get(1).getTotalFans()).isEqualTo(10010);
    }

    @Test
    public void reset_loyalty_test() {
        Team team = Team.builder().fans(Team.Fans.builder().build()).build();
        team.getFans().getFanTiers().put(10, Team.Fans.FanTier.builder().loyalty(100.0).build());
        team.getFans().resetLoyalty();
        assertThat(team.getFans().getFanTiers().get(1).getLoyalty()).isEqualTo(50.0);
    }

    // ********* End Fans Tests *********


    @Test
    public void should_throw_error_if_stadium_have_max_seats() {
        Team team = Team.builder().buildings(Team.Buildings.builder().stadium(
            new Team.Buildings.Stadium()).build()).build();
        team.getBuildings().getStadium().setSeats(100000);

        assertEquals("Stadium already have maximum level", assertThrows(RuntimeException.class, () -> {
            team.getBuildings().getStadium().increaseLevel();
        }).getMessage());
    }

    @Test
    public void should_set_maintenance_cost_maximum() {
        Team team = Team.builder().buildings(Team.Buildings.builder().stadium(
            new Team.Buildings.Stadium()).build()).build();
        team.getBuildings().getStadium().setSeats(99000);
        team.getBuildings().getStadium().increaseLevel();
        assertEquals(BigDecimal.valueOf(300000), team.getBuildings().getStadium().getMaintenanceCost());
    }

    @Test
    public void should_update_seats_and_cost() {
        Team team = Team.builder().buildings(Team.Buildings.builder().stadium(
            new Team.Buildings.Stadium()).build()).build();
        team.getBuildings().getStadium().increaseLevel();
        assertEquals(BigDecimal.valueOf(6000), team.getBuildings().getStadium().getMaintenanceCost());
        assertEquals(6000, team.getBuildings().getStadium().getSeats());
    }

    @Test
    void should_get_highest_season_number() {
        Team team = Team.builder().leagueStats(Map.of(
            1, Team.LeagueStats.builder().build(),
            4, Team.LeagueStats.builder().build(),
             5, Team.LeagueStats.builder().build()
        )).build();
        assertEquals(5, team.getActualSeason());
    }

    @Test
    void should_get_actual_season_position() {
        Team team = Team.builder().leagueStats(Map.of(
            1, Team.LeagueStats.builder().tablePosition(2).build(),
            4, Team.LeagueStats.builder().tablePosition(6).build()
        )).build();
        assertEquals(6, team.getActualSeasonTablePosition());
    }

    @Test
    void should_get_previous_season_position() {
        Team team = Team.builder().leagueStats(Map.of(
            3, Team.LeagueStats.builder().tablePosition(2).build(),
            4, Team.LeagueStats.builder().tablePosition(6).build()
        )).build();
        assertEquals(2, team.getLastSeasonPosition());
    }

    @Test
    void should_get_last_table_position_when_prev_season_missing() {
        Team team = Team.builder().leagueStats(Map.of(
            3, Team.LeagueStats.builder().tablePosition(2).build()
        )).build();
        assertEquals(12, team.getLastSeasonPosition());
    }
}
