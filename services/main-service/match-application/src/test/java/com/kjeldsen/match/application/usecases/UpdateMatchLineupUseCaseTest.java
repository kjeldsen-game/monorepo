package com.kjeldsen.match.application.usecases;

import com.kjeldsen.lib.clients.PlayerClientApi;
import com.kjeldsen.lib.model.player.PlayerClient;
import com.kjeldsen.lib.model.player.PlayerSkillsClient;
import com.kjeldsen.match.application.usecases.common.BaseClientTest;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.modifers.HorizontalPressure;
import com.kjeldsen.match.domain.modifers.Tactic;
import com.kjeldsen.match.domain.modifers.TeamModifiers;
import com.kjeldsen.match.domain.modifers.VerticalPressure;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import com.kjeldsen.player.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UpdateMatchLineupUseCaseTest extends BaseClientTest {

    private final MatchWriteRepository mockedMatchWriteRepository  = Mockito.mock(MatchWriteRepository.class);
    private final GetMatchTeamUseCase mockedGetMatchTeamUseCase = Mockito.mock(GetMatchTeamUseCase.class);
    private final PlayerClientApi mockedPlayerClient = Mockito.mock(PlayerClientApi.class);
    private final UpdateMatchLineupUseCase updateMatchLineupUseCase = new UpdateMatchLineupUseCase(
        mockedMatchWriteRepository, mockedGetMatchTeamUseCase, mockedPlayerClient);


    @Test
    @DisplayName("Should update the match lineup")
    void should_update_the_lineup() {

        List<UpdateMatchLineupUseCase.PlayerUpdateDTO> players = List.of(
            UpdateMatchLineupUseCase.PlayerUpdateDTO.builder().id("player1").status(PlayerStatus.ACTIVE)
                .playerOrder(PlayerOrder.NONE).position(PlayerPosition.GOALKEEPER).build(),
            UpdateMatchLineupUseCase.PlayerUpdateDTO.builder().id("player2").status(PlayerStatus.BENCH)
                .playerOrder(PlayerOrder.NONE).position(PlayerPosition.GOALKEEPER).build(),
            UpdateMatchLineupUseCase.PlayerUpdateDTO.builder().id("player3").status(PlayerStatus.INACTIVE)
                .playerOrder(PlayerOrder.NONE).position(PlayerPosition.GOALKEEPER).build());

        TeamModifiers teamModifiers = new TeamModifiers(
            VerticalPressure.NO_VERTICAL_FOCUS, HorizontalPressure.NO_HORIZONTAL_FOCUS, Tactic.TIKA_TAKA);

        GetMatchTeamUseCase.MatchAndTeam matchAndTeam = new GetMatchTeamUseCase.MatchAndTeam(
            Match.builder().build(),
            Team.builder().build(),
            TeamRole.HOME);

        when(mockedGetMatchTeamUseCase.getMatchAndTeam("matchId", "teamId")).thenReturn(
            matchAndTeam);

        List<PlayerClient> dtos = new ArrayList<>();
        PlayerSkillsClient skillPoints = new PlayerSkillsClient(50, 0, "RESIDUAL");

        for (int a = 1; a <= players.size(); a++) {
            UpdateMatchLineupUseCase.PlayerUpdateDTO player = players.get(a-1);
            dtos.add(PlayerClient.builder()
                .id("player" + a)
                .actualSkills(Map.of("SCORING", skillPoints))
                .name("player" + a)
                .teamId("teamId")
                .playerOrder("NONE")
                .status(player.getStatus().name())
                .position(player.getPosition().name())
                .build());
        }

        when(mockedPlayerClient.getPlayers("teamId")).thenReturn(dtos);
        updateMatchLineupUseCase.update("matchId", "teamId", players, teamModifiers);

        assertTrue(matchAndTeam.team().getSpecificLineup());
        assertEquals(VerticalPressure.NO_VERTICAL_FOCUS, matchAndTeam.team().getVerticalPressure());
        assertEquals(HorizontalPressure.NO_HORIZONTAL_FOCUS, matchAndTeam.team().getHorizontalPressure());
        assertEquals(Tactic.TIKA_TAKA, matchAndTeam.team().getTactic());
        assertEquals(1, matchAndTeam.team().getPlayers().size());
        assertEquals(1, matchAndTeam.team().getBench().size());
        assertEquals("player1", matchAndTeam.team().getPlayers().get(0).getId());
    }
}