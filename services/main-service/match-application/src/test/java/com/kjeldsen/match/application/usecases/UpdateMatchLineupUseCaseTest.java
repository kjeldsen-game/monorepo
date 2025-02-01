package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.modifers.HorizontalPressure;
import com.kjeldsen.match.domain.modifers.Tactic;
import com.kjeldsen.match.domain.modifers.TeamModifiers;
import com.kjeldsen.match.domain.modifers.VerticalPressure;
import com.kjeldsen.match.domain.repositories.MatchReadRepository;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UpdateMatchLineupUseCaseTest {

    private final MatchWriteRepository mockedMatchWriteRepository  = Mockito.mock(MatchWriteRepository.class);
    private final PlayerReadRepository mockedPlayerReadRepository  = Mockito.mock(PlayerReadRepository.class);
    private final GetMatchTeamUseCase mockedGetMatchTeamUseCase = Mockito.mock(GetMatchTeamUseCase.class);
    private final UpdateMatchLineupUseCase updateMatchLineupUseCase = new UpdateMatchLineupUseCase(
        mockedMatchWriteRepository, mockedPlayerReadRepository, mockedGetMatchTeamUseCase);


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


        PlayerSkills skillPoints = new PlayerSkills(50, 0, PlayerSkillRelevance.RESIDUAL);
        for (int a = 1; a <= players.size(); a++) {
            UpdateMatchLineupUseCase.PlayerUpdateDTO player = players.get(a-1);
            when(mockedPlayerReadRepository.findOneById(Player.PlayerId.of("player" + a)))
                .thenReturn(Optional.ofNullable(Player.builder()
                    .id(Player.PlayerId.of(player.getId()))
                    .name("player" + a)
                    .teamId(com.kjeldsen.player.domain.Team.TeamId.of("teamId"))
                    .actualSkills(new HashMap<>(Map.of(PlayerSkill.SCORING, skillPoints)))
                    .status(PlayerStatus.valueOf(player.getStatus().name()))
                    .playerOrder(PlayerOrder.valueOf(player.getPlayerOrder().name()))
                    .position(PlayerPosition.valueOf(player.getPosition().name()))
                    .build()));
        }

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