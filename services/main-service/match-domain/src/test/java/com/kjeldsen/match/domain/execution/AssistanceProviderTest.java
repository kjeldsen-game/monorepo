package com.kjeldsen.match.domain.execution;

import com.kjeldsen.match.domain.entities.DuelStats;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.duel.DuelRole;
import com.kjeldsen.match.domain.state.BallHeight;
import com.kjeldsen.match.domain.state.BallState;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.match.domain.state.TeamState;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AssistanceProviderTest {

    @Test
    @DisplayName("Should return adjusted team assistance values")
    void should_return_adjusted_team_assistance() {

        Map<String, Double> initiatorTeamAssistance = Map.of(
            "Alice", 50.0,
            "Bob", 50.0,
            "Charlie", 50.0,
            "Diana", 50.0
        );

        Map<String, Double> challengerTeamAssistance = Map.of(
            "Alice", 50.0,
            "Bob", 50.0,
            "Charlie", 20.0,
            "Diana", 10.0
        );

        Map<DuelRole, DuelStats.Assistance> result = AssistanceProvider.buildAssistanceByDuelRole(
            initiatorTeamAssistance, challengerTeamAssistance, new HashMap<>());

        assertEquals(130.0 , result.get(DuelRole.CHALLENGER).getTotal());
        assertEquals(200 , result.get(DuelRole.INITIATOR).getTotal());
        assertEquals(0.0, result.get(DuelRole.CHALLENGER).getAdjusted());
        assertEquals(AssistanceProvider.TEAM_ASSISTANCE_SCALING.get(70), result.get(DuelRole.INITIATOR).getAdjusted());
    }

    @Test
    @DisplayName("Should return team assistance for one duel role")
    void should_return_team_assistance_for_one_duel_role() {
        TeamState teamState = TeamState.builder().id("id").players(List.of(
            Player.builder().id("id1").name("name").position(PlayerPosition.FORWARD)
                .skills(Map.of(
                    PlayerSkill.OFFENSIVE_POSITIONING, 12,
                    PlayerSkill.BALL_CONTROL, 12))
                .build(),
            Player.builder().id("id2").name("name3").position(PlayerPosition.CENTRE_BACK)
                .skills(Map.of(
                    PlayerSkill.OFFENSIVE_POSITIONING, 12,
                    PlayerSkill.BALL_CONTROL, 12))
                .build()
        )).build();
        Player mockedPlayer = Player.builder().id("id2").name("name3").build();

        GameState mockedGameState = Mockito.mock(GameState.class);
        Mockito.when(mockedGameState.attackingTeam()).thenReturn(teamState);
        Mockito.when(mockedGameState.getBallState()).thenReturn(new BallState(
            mockedPlayer, PitchArea.LEFT_FORWARD, BallHeight.GROUND));

        Map<String, Double> result = AssistanceProvider.getTeamAssistance(mockedGameState,
            mockedPlayer, DuelRole.INITIATOR);
        assertEquals(24.0 * 0.5, result.get("name"));
    }
}