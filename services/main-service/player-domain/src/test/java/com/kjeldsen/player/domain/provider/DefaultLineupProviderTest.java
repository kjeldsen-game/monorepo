package com.kjeldsen.player.domain.provider;

import com.kjeldsen.player.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultLineupProviderTest {

    @Test
    @DisplayName("Should set default lineup players")
    void should_set_default_lineup_players() {
        List<Player> playerList = new ArrayList<>();
        for(int i = 0; i < 30; i ++) {
            playerList.add(PlayerProvider.generate(Team.TeamId.of("teamId"), PlayerPositionTendency.getDefault(
                PlayerProvider.position()), PlayerCategory.SENIOR,200));
        }

        DefaultLineupProvider.set(playerList);

        assertThat(playerList.stream().filter(player -> player.getStatus()
            .equals(PlayerStatus.ACTIVE))).hasSize(11);
        assertThat(playerList.stream().filter(player -> player.getStatus()
            .equals(PlayerStatus.BENCH))).hasSize(7);
        assertThat(playerList.stream().filter(player -> player.getStatus()
            .equals(PlayerStatus.INACTIVE))).hasSize(12);

        assertThat(playerList.stream().filter(player -> player.getPosition() != null))
            .hasSize(11);
    }
}