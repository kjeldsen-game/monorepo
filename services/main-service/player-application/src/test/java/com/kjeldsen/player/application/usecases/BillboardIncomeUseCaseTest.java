package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillboardIncomeUseCaseTest {

    private final TeamReadRepository mockedTeamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final TeamWriteRepository mockedTeamWriteRepository = Mockito.mock(TeamWriteRepository.class);
    private final CreateTransactionUseCase mockedCreateTransactionUseCase = Mockito.mock(CreateTransactionUseCase.class);
    private final BillboardIncomeUseCase billboardIncomeUseCase = new BillboardIncomeUseCase(
            mockedTeamReadRepository, mockedTeamWriteRepository, mockedCreateTransactionUseCase
    );

    private static Team.TeamId mockedTeamId;

    @BeforeAll
    static void setUpBeforeClass() {
        mockedTeamId = TestData.generateTestTeamId();
    }

    @Test
    @DisplayName("Should throw exception when team is null")
    public void should_throw_exception_when_team_is_null() {

        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.empty());

        assertEquals("Team not found", assertThrows(RuntimeException.class, () -> {
            billboardIncomeUseCase.incomeWinBonus(mockedTeamId, 1);
        }).getMessage());

        assertEquals("Team not found", assertThrows(RuntimeException.class, () -> {
            billboardIncomeUseCase.incomeSelection(mockedTeamId, Team.Economy.IncomeMode.CONSERVATIVE,
                    Team.Economy.IncomePeriodicity.WEEKLY);}).getMessage());
        verify(mockedTeamReadRepository, times(2)).findById(mockedTeamId);
        verifyNoMoreInteractions(mockedTeamReadRepository);
    }

    @Test
    @DisplayName("Should throw exception when the mode is not defined")
    public void should_throw_exception_when_the_mode_is_not_defined() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.of(mockedTeam));

        billboardIncomeUseCase.incomeWinBonus(mockedTeamId, 1);

        verify(mockedCreateTransactionUseCase, never()).create(any(), any(), any());
    }

    @Test
    @DisplayName("Should not execute the createTransactionUseCase for weekly amounts are 0 bonus income")
    public void should_not_execute_the_createTransactionUseCase_because_amounts_are_zero() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        mockedTeam.getEconomy().setBillboards(Map.of(
                Team.Economy.IncomePeriodicity.ANNUAL, Team.Economy.IncomeMode.MODERATE,
                Team.Economy.IncomePeriodicity.WEEKLY, Team.Economy.IncomeMode.CONSERVATIVE
        ));
        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.of(mockedTeam));

        billboardIncomeUseCase.incomeWinBonus(mockedTeamId, 1);

        verify(mockedCreateTransactionUseCase, times(1)).create(any(), any(), any());
    }

    @Test
    @DisplayName("Should pass the variables to the CreateTransactionUseCase incomeWinBonus")
    public void should_pass_variables_to_the_CreateTransactionUseCase_income_win_bonus() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        mockedTeam.getEconomy().setBillboards(Map.of(
            Team.Economy.IncomePeriodicity.ANNUAL, Team.Economy.IncomeMode.MODERATE,
            Team.Economy.IncomePeriodicity.WEEKLY, Team.Economy.IncomeMode.MODERATE
        ));
        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.of(mockedTeam));

        billboardIncomeUseCase.incomeWinBonus(mockedTeamId, 1);

        ArgumentCaptor<Team.TeamId> teamIdCaptor = ArgumentCaptor.forClass(Team.TeamId.class);
        ArgumentCaptor<BigDecimal> amountCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        ArgumentCaptor<Transaction.TransactionType> transactionTypeCaptor = ArgumentCaptor.forClass(Transaction.TransactionType.class);

        verify(mockedCreateTransactionUseCase, times(2)).create(
            teamIdCaptor.capture(),
            amountCaptor.capture(),
            transactionTypeCaptor.capture()
        );

        assertEquals(mockedTeamId, teamIdCaptor.getAllValues().get(0));
        assertEquals(BigDecimal.valueOf(100_000), amountCaptor.getAllValues().get(0));
        assertEquals(Transaction.TransactionType.BILLBOARDS, transactionTypeCaptor.getAllValues().get(0));

        assertEquals(mockedTeamId, teamIdCaptor.getAllValues().get(1));
        assertEquals(BigDecimal.valueOf(200_000), amountCaptor.getAllValues().get(1));
        assertEquals(Transaction.TransactionType.BILLBOARDS, transactionTypeCaptor.getAllValues().get(1));
    }

    @Test
    @DisplayName("Should not execute the createTransactionUseCase for weekly amounts are 0 select income")
    public void should_not_execute_the_createTransactionUseCase_because_amounts_are_zero_selection_income() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.of(mockedTeam));

        billboardIncomeUseCase.incomeSelection(mockedTeamId, Team.Economy.IncomeMode.AGGRESSIVE,
            Team.Economy.IncomePeriodicity.WEEKLY);

        verify(mockedCreateTransactionUseCase, never()).create(any(), any(), any());
    }

    @Test
    @DisplayName("Should set the Weekly Mode and pass variables to createTransactionUseCase")
    public void should_set_the_weekly_mode_and_pass_variables_to_createTransactionUseCase() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.of(mockedTeam));

        billboardIncomeUseCase.incomeSelection(mockedTeamId, Team.Economy.IncomeMode.CONSERVATIVE,
            Team.Economy.IncomePeriodicity.WEEKLY);

        verify(mockedCreateTransactionUseCase).create(
            eq(mockedTeamId),
            eq(BigDecimal.valueOf(100_000)),
            eq(Transaction.TransactionType.BILLBOARDS));

        assertEquals(Team.Economy.IncomeMode.CONSERVATIVE,
            mockedTeam.getEconomy().getBillboards().get(Team.Economy.IncomePeriodicity.WEEKLY));
        verify(mockedTeamReadRepository).findById(mockedTeamId);
    }
}