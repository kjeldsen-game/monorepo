package com.kjeldsen.player.domain;

import com.kjeldsen.player.domain.events.FansEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
}
