package com.kjeldsen.match.domain.entities;

import com.kjeldsen.match.common.RandomHelper;
import com.kjeldsen.player.domain.PlayerPosition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TeamTest {


    @Test
    @DisplayName("Should return players")
    void should_return_players() {
        // Create a team with one Goalkeeper
        Team team = RandomHelper.genTeam(TeamRole.HOME);

        List<Player> expectedPlayers = team.getPlayers(PlayerPosition.GOALKEEPER);
        assertThat(expectedPlayers).hasSize(1);
    }

    @Test
    @DisplayName("Should create a deep copy of team")
    void should_create_deep_copy_of_team() {
        Team team = RandomHelper.genTeam(TeamRole.HOME);
        Team copy = team.deepCopy();
        assertThat(team.getId()).isEqualTo(copy.getId());
        assertThat(team.getName()).isEqualTo(copy.getName());
        assertThat(team.getRole()).isEqualTo(copy.getRole());
        // TODO add also players and bench
    }
}