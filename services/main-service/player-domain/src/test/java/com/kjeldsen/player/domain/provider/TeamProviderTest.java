package com.kjeldsen.player.domain.provider;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.Team;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class TeamProviderTest {

    @Test
    @DisplayName("Should create team players with correct ratings")
    void test() {
        List<Player> players = TeamProvider.provide(Team.TeamId.of("teamId"));
        assertThat(players).hasSize(32);
    }

    static Stream<Arguments> countArguments() {
        return Stream.of(
            Arguments.of(List.of(PlayerPosition.GOALKEEPER), 3),
            Arguments.of(List.of(PlayerPosition.CENTRE_BACK), 5),
            Arguments.of(List.of(PlayerPosition.LEFT_BACK, PlayerPosition.RIGHT_BACK), 4),
            Arguments.of(List.of(PlayerPosition.LEFT_WINGBACK, PlayerPosition.RIGHT_WINGBACK), 4),
            Arguments.of(List.of(PlayerPosition.DEFENSIVE_MIDFIELDER), 3),
            Arguments.of(List.of(PlayerPosition.CENTRE_MIDFIELDER), 5),
            Arguments.of(List.of(PlayerPosition.OFFENSIVE_MIDFIELDER), 1),
            Arguments.of(List.of(PlayerPosition.FORWARD, PlayerPosition.STRIKER), 4),
            Arguments.of(List.of(PlayerPosition.LEFT_WINGER, PlayerPosition.RIGHT_WINGER), 3)
        );
    }

    @ParameterizedTest
    @MethodSource("countArguments")
    @DisplayName("Should test counts of the players")
    void should_test_players_counts(List<PlayerPosition> positions, int expectedSize) {
        List<Player> players = TeamProvider.provide(Team.TeamId.of("teamId"));
        assertThat(players.stream().filter(player ->
            positions.contains(player.getPreferredPosition())).toList()).hasSize(expectedSize);
    }
}