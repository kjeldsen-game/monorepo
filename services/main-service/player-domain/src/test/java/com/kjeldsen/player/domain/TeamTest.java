package com.kjeldsen.player.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

}
