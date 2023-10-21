package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.CanteraInvestmentEvent;
import com.kjeldsen.player.domain.repositories.CanteraInvestmentEventWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CanteraTraditionInvestmentUsecaseTest {

    private final TeamReadRepository teamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final TeamWriteRepository teamWriteRepository = Mockito.mock(TeamWriteRepository.class);
    private final CanteraInvestmentEventWriteRepository canteraInvestmentEventWriteRepository = Mockito.mock(CanteraInvestmentEventWriteRepository.class);
    private final CanteraTraditionInvestmentUsecase canteraTraditionInvestmentUsecase = new CanteraTraditionInvestmentUsecase(teamReadRepository, teamWriteRepository, canteraInvestmentEventWriteRepository);

    @Test
    public void should_throw_exception_when_teamId_does_not_found() {
        String teamId = "exampleTeamId";
        String errorMessage = "Team not found";
        Integer points = 20;
        when(teamReadRepository.findById(Team.TeamId.of(teamId))).thenThrow(new RuntimeException(errorMessage));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            canteraTraditionInvestmentUsecase.invest(Team.TeamId.of(teamId), points);
        });
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void should_call_respositories_when_teamId_found() {
        Team.TeamId teamId = Team.TeamId.generate();
        Integer points = 30;
        Team team = Team.builder()
            .id(teamId)
            .name("exampleName")
            .userId("exampleUserId")
            .cantera(Team.Cantera.builder()
                .score(.0)
                .economyLevel(100)
                .traditionLevel(30)
                .buildingsLevel(500)
                .build())
            .build();
        when(teamReadRepository.findById(teamId)).thenReturn(Optional.of(team));
        canteraTraditionInvestmentUsecase.invest(teamId, points);
        verify(canteraInvestmentEventWriteRepository, Mockito.times(1)).save(Mockito.any());
        verify(teamWriteRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void should_save_the_team_what_was_found_and_save_investEvent() {
        Integer points = 10;
        Team.TeamId teamId = Team.TeamId.generate();
        String userId = "exampleUserId";
        Team team2 = Team.builder()
            .id(teamId)
            .userId(userId)
            .name("exampleName")
            .economy(Team.Economy.builder()
                .balance(new BigDecimal("1000000.00"))
                .build())
            .cantera(Team.Cantera.builder()
                .score(.0)
                .economyLevel(100)
                .traditionLevel(30)
                .buildingsLevel(500)
                .build())
            .build();
        teamWriteRepository.save(team2);
        when(canteraInvestmentEventWriteRepository.save(Mockito.any()))
            .thenReturn(CanteraInvestmentEvent.builder()
                .teamId(teamId)
                .investment(Team.Cantera.Investment.TRADITION)
                .points(points)
                .build());
        when(teamWriteRepository.save(Mockito.any())).thenReturn(team2);
        when(teamReadRepository.findById(teamId)).thenReturn(Optional.of(team2));
        canteraTraditionInvestmentUsecase.invest(teamId, points);
        verify(canteraInvestmentEventWriteRepository, Mockito.times(1)).save(Mockito.any());
        verify(teamWriteRepository, Mockito.times(2)).save(Mockito.any());
    }
}
