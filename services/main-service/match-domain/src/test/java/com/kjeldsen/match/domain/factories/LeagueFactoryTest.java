package com.kjeldsen.match.domain.factories;

import com.kjeldsen.match.domain.entities.League;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LeagueFactoryTest {


    @ParameterizedTest
    @EnumSource(League.LeagueStatus.class)
    @DisplayName("Should create a league with status")
    void should_create_a_league_with_status(League.LeagueStatus status) {
        League league = LeagueFactory.createLeague(status);
        assertNotNull(league);
        assertThat(league.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Should create a league stats for team in league")
    void should_create_a_league_stats_for_team() {
        League league = LeagueFactory.createLeague(League.LeagueStatus.PRESEASON);
        LeagueFactory.createLeagueStats(league, "team1", "teamid");
        assertThat(league.getTeams().size()).isEqualTo(1);
        assertThat(league.getTeams().get("teamid")).isNotNull();
    }
}