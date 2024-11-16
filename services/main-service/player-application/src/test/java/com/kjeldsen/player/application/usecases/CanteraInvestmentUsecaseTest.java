package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.application.usecases.cantera.CanteraInvestmentUsecase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.CanteraInvestmentEvent;
import com.kjeldsen.player.domain.repositories.CanteraInvestmentEventWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class CanteraInvestmentUsecaseTest {

    private final TeamReadRepository mockedTeamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final TeamWriteRepository mockedTeamWriteRepository = Mockito.mock(TeamWriteRepository.class);
    private final CanteraInvestmentEventWriteRepository mockedCanteraInvestmentEventWriteRepository = Mockito.mock(CanteraInvestmentEventWriteRepository.class);
    private final CanteraInvestmentUsecase canteraInvestmentUsecase = new CanteraInvestmentUsecase(mockedTeamReadRepository,
          mockedTeamWriteRepository, mockedCanteraInvestmentEventWriteRepository);

    private static Team.TeamId mockedTeamId;

    @BeforeAll
    static void setUpBeforeTestClass() {
        mockedTeamId = TestData.generateTestTeamId();
    }

    @Test
    @DisplayName("Should throw exception when team is null")
    public void should_throw_exception_when_team_is_null() {

        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.empty());

        assertEquals("Team not found", assertThrows(RuntimeException.class, () -> {
            canteraInvestmentUsecase.investToCanteraCategory(mockedTeamId, Team.Cantera.Investment.BUILDINGS,10);
        }).getMessage());
        verify(mockedTeamReadRepository).findById(mockedTeamId);
        verifyNoMoreInteractions(mockedTeamReadRepository);
    }

    @Test
    @DisplayName("Should add the points to the investment buildings")
    public void should_add_points_to_the_investment_buildings() {
        addPointsToCanteraInvestment(Team.Cantera.Investment.BUILDINGS);
    }

    @Test
    @DisplayName("Should add the points to the investment economy")
    public void should_add_points_to_the_investment_economy() {
        addPointsToCanteraInvestment(Team.Cantera.Investment.ECONOMY);
    }

    @Test
    @DisplayName("Should add the points to the investment tradition")
    public void should_add_points_to_the_investment_tradition() {
        addPointsToCanteraInvestment(Team.Cantera.Investment.TRADITION);
    }

    private void addPointsToCanteraInvestment(Team.Cantera.Investment canteraInvestment) {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.of(mockedTeam));

        canteraInvestmentUsecase.investToCanteraCategory(mockedTeamId, canteraInvestment, 20);

        switch (canteraInvestment) {
            case BUILDINGS -> assertEquals(20, mockedTeam.getCantera().getBuildingsLevel());
            case TRADITION -> assertEquals(20, mockedTeam.getCantera().getTraditionLevel());
            case ECONOMY -> assertEquals(20, mockedTeam.getCantera().getEconomyLevel());
        }

        ArgumentCaptor<CanteraInvestmentEvent> canteraInvestmentEvent = ArgumentCaptor.forClass(CanteraInvestmentEvent.class);
        verify(mockedCanteraInvestmentEventWriteRepository).save(canteraInvestmentEvent.capture());
        CanteraInvestmentEvent capturedInvestmentEvent = canteraInvestmentEvent.getValue();

        assertEquals(mockedTeamId, capturedInvestmentEvent.getTeamId());
        assertEquals(canteraInvestment, capturedInvestmentEvent.getInvestment());

        verify(mockedTeamReadRepository).findById(mockedTeamId);
        verify(mockedTeamWriteRepository).save(mockedTeam);
        verifyNoMoreInteractions(mockedTeamReadRepository,
                mockedCanteraInvestmentEventWriteRepository, mockedTeamWriteRepository);
    }
}