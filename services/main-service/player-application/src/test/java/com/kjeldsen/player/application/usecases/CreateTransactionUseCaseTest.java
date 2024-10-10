package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CreateTransactionUseCaseTest {

    private final TeamReadRepository mockedTeamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final TeamWriteRepository mockedTeamWriteRepository = Mockito.mock(TeamWriteRepository.class);
    private final TransactionWriteRepository mockedTransactionReadRepository = Mockito.mock(TransactionWriteRepository.class);
    private final TransactionEventWriteRepository mockedTransactionEventWriteRepository = Mockito.mock(TransactionEventWriteRepository.class);
    private final CreateTransactionUseCase createTransactionUseCase = new CreateTransactionUseCase(
            mockedTeamReadRepository, mockedTeamWriteRepository, mockedTransactionReadRepository, mockedTransactionEventWriteRepository
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

        assertEquals("Team not found", assertThrows(RuntimeException.class, () -> createTransactionUseCase.create(mockedTeamId, BigDecimal.TEN, Transaction.TransactionType.SPONSOR)).getMessage());
        verify(mockedTeamReadRepository).findById(mockedTeamId);
        verifyNoMoreInteractions(mockedTeamReadRepository);
    }

    @Test
    @DisplayName("Should update team balance on income transaction")
    public void should_update_team_balance_on_income_transaction() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.of(mockedTeam));

        createTransactionUseCase.create(mockedTeamId, BigDecimal.TEN, Transaction.TransactionType.SPONSOR);

        assertEquals(BigDecimal.valueOf(10), mockedTeam.getEconomy().getBalance());
        verify(mockedTeamReadRepository).findById(mockedTeamId);
        verifyNoMoreInteractions(mockedTeamReadRepository);
    }

    @Test
    @DisplayName("Should update team balance to negative when expense type is PLAYER_WAGE")
    public void should_update_team_balance_to_negative_when_expense_type_is_PLAYER_WAGE() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.of(mockedTeam));

        createTransactionUseCase.create(mockedTeamId, BigDecimal.valueOf(-10_000),
            Transaction.TransactionType.PLAYER_WAGE);

        assertEquals(BigDecimal.valueOf(-10_000), mockedTeam.getEconomy().getBalance());
        verify(mockedTeamReadRepository).findById(mockedTeamId);
        verifyNoMoreInteractions(mockedTeamReadRepository);
    }
}