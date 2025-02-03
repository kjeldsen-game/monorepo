package com.kjeldsen.match.domain.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class LeagueTest {

    @Test
    @DisplayName("Should return not empty or not null leagueId")
    public void should_return_not_empty_or_not_null_leagueId_value_on_generate_method() {
        League.LeagueId leagueId = League.LeagueId.generate();
        Assertions.assertNotNull(leagueId.value());
        Assertions.assertFalse(leagueId.value().isEmpty());
    }

    @Test
    @DisplayName("Should return a unique leagueId")
    public void should_return_a_unique_leagueId_value_on_generate_method() {
        League.LeagueId leagueId1 = League.LeagueId.generate();
        League.LeagueId leagueId2= League.LeagueId.generate();
        Assertions.assertNotEquals(leagueId2.value(), leagueId1.value());
    }

    @Test
    @DisplayName("Should assign a correct value on leagueIdOfMethod ")
    public void should_assign_a_correct_value_on_leagueIdOf_method() {
        String exampleTeamId = "exampleId";
        League.LeagueId leagueId = League.LeagueId.of(exampleTeamId);
        Assertions.assertEquals(exampleTeamId, leagueId.value());
    }

    @Test
    @DisplayName("Should throw error when league is already full")
    void should_throw_error_when_league_is_already_full() {
        League testLeague = League.builder().teams(new HashMap<>()).build();
        for (int a = 0; a < 10; a++) {
            testLeague.getTeams().put(String.valueOf(a), new League.LeagueStats());
        }
        assertEquals("League is already full", assertThrows(IllegalArgumentException.class, () -> {
            testLeague.addTeam("teamId", new League.LeagueStats());
        }).getMessage());

   }

    @Test
    @DisplayName("Should add team to the league")
    void should_add_team_to_the_league() {
        League testLeague = League.builder().teams(new HashMap<>()).build();
        testLeague.addTeam("teamId", new League.LeagueStats());
        assertEquals(1, testLeague.getTeams().size());
    }

    @Test
    @DisplayName("Should update the scored and received goals")
    void should_update_scored_and_received_goals() {
        League testLeague = League.builder().teams(new HashMap<>()).build();
        testLeague.addTeam("teamId", new League.LeagueStats());
        testLeague.getTeams().get("teamId").updateGoals(1,2);
        assertEquals(1, testLeague.getTeams().get("teamId").getGoalsScored());
        assertEquals(2, testLeague.getTeams().get("teamId").getGoalsReceived());
    }

    @Test
    @DisplayName("Should handle win")
    void should_handle_win() {
        League testLeague = League.builder().teams(new HashMap<>()).build();
        testLeague.addTeam("teamId", new League.LeagueStats());
        testLeague.getTeams().get("teamId").handleWin();
        assertEquals(1, testLeague.getTeams().get("teamId").getWins());
        assertEquals(0, testLeague.getTeams().get("teamId").getLosses());
        assertEquals(0, testLeague.getTeams().get("teamId").getDraws());
        assertEquals(3, testLeague.getTeams().get("teamId").getPoints());
        assertEquals(1, testLeague.getTeams().get("teamId").getGamesPlayed());
    }

    @Test
    @DisplayName("Should handle loss")
    void should_handle_loss() {
        League testLeague = League.builder().teams(new HashMap<>()).build();
        testLeague.addTeam("teamId", new League.LeagueStats());
        testLeague.getTeams().get("teamId").handleLoss();
        assertEquals(0, testLeague.getTeams().get("teamId").getWins());
        assertEquals(1, testLeague.getTeams().get("teamId").getLosses());
        assertEquals(0, testLeague.getTeams().get("teamId").getDraws());
        assertEquals(0, testLeague.getTeams().get("teamId").getPoints());
        assertEquals(1, testLeague.getTeams().get("teamId").getGamesPlayed());
    }

    @Test
    @DisplayName("Should handle draw")
    void should_handle_draw() {
        League testLeague = League.builder().teams(new HashMap<>()).build();
        testLeague.addTeam("teamId", new League.LeagueStats());
        testLeague.getTeams().get("teamId").handleDraw();
        assertEquals(0, testLeague.getTeams().get("teamId").getWins());
        assertEquals(0, testLeague.getTeams().get("teamId").getLosses());
        assertEquals(1, testLeague.getTeams().get("teamId").getDraws());
        assertEquals(1, testLeague.getTeams().get("teamId").getPoints());
        assertEquals(1, testLeague.getTeams().get("teamId").getGamesPlayed());
    }
}