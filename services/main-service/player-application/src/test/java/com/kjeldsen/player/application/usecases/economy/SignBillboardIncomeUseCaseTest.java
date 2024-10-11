package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SignBillboardIncomeUseCaseTest {

    private final TeamReadRepository mockedTeamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final TeamWriteRepository mockedTeamWriteRepository = Mockito.mock(TeamWriteRepository.class);
    private final BillboardIncomeUseCase mockedBillboardIncomeUseCase = Mockito.mock(BillboardIncomeUseCase.class);
    private final SignBillboardIncomeUseCase signBillboardIncomeUseCase = new SignBillboardIncomeUseCase(
        mockedTeamReadRepository, mockedTeamWriteRepository, mockedBillboardIncomeUseCase);

    private static Team.TeamId mockedTeamId;

    @BeforeAll
    static void setUp() {
        mockedTeamId = TestData.generateTestTeamId();
    }

    @Test
    @DisplayName("Should throw exception when team is null")
    void should_throw_exception_when_team_is_null() {
        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.empty());

        assertEquals("Team not found", assertThrows(RuntimeException.class, () -> {
            signBillboardIncomeUseCase.sign(mockedTeamId, Team.Economy.BillboardIncomeType.LONG);
        }).getMessage());

        verify(mockedTeamReadRepository).findById(mockedTeamId);
        verifyNoMoreInteractions(mockedTeamReadRepository);
    }


    @Test
    @DisplayName("Should throw exception when billboard deal is not null")
    void should_throw_exception_when_billboard_deal_is_not_null() {
        Team mockedTeam = Mockito.mock(Team.class);
        Team.Economy mockedEconomy = Mockito.mock(Team.Economy.class);

        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.of(mockedTeam));
        when(mockedTeam.getEconomy()).thenReturn(mockedEconomy);
        when(mockedEconomy.getBillboardDeal()).thenReturn(Team.Economy.BillboardDeal.builder().build());

        assertEquals("Billboard deal is already set", assertThrows(RuntimeException.class, () -> {
            signBillboardIncomeUseCase.sign(mockedTeamId, Team.Economy.BillboardIncomeType.LONG);
        }).getMessage());
        verify(mockedTeamReadRepository).findById(mockedTeamId);
        verifyNoMoreInteractions(mockedTeamReadRepository);
    }

    @Test
    @DisplayName("Should sign billboard deal and pay")
    void should_sign_billboard_deal_and_pay() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        mockedTeam.getLeagueStats().clear();
        mockedTeam.getLeagueStats().put(1, Team.LeagueStats.builder().tablePosition(1).build());

        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.of(mockedTeam));

        signBillboardIncomeUseCase.sign(mockedTeamId, Team.Economy.BillboardIncomeType.LONG);

        assertEquals(1, mockedTeam.getEconomy().getBillboardDeal().getStartSeason());
        assertEquals(4, mockedTeam.getEconomy().getBillboardDeal().getEndSeason());
        assertEquals(Team.Economy.BillboardIncomeType.LONG, mockedTeam.getEconomy().getBillboardDeal().getType());
        assertTrue(mockedTeam.getEconomy().getBillboardDeal().getOffer().compareTo(BigDecimal.ZERO) > 0) ;
        verify(mockedTeamReadRepository).findById(mockedTeamId);
        verify(mockedBillboardIncomeUseCase).pay(eq(mockedTeamId));
    }
}