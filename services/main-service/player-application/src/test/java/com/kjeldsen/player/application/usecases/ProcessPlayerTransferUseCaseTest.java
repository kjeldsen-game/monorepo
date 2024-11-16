package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.application.usecases.economy.CreateTransactionUseCase;
import com.kjeldsen.player.application.usecases.player.ProcessPlayerTransferUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcessPlayerTransferUseCaseTest {

    private final PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final PlayerWriteRepository mockedPlayerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    private final CreateTransactionUseCase mockedCreateTransactionUseCase = Mockito.mock(CreateTransactionUseCase.class);
    private final ProcessPlayerTransferUseCase processPlayerTransferUseCase = new ProcessPlayerTransferUseCase(
            mockedPlayerReadRepository, mockedPlayerWriteRepository, mockedCreateTransactionUseCase);

    private static Team.TeamId mockedWinnerTeamId;
    private static Team.TeamId mockedCreatorTeamId;
    private static Player.PlayerId mockedPlayerId;


    @BeforeAll
    static void setUpBeforeClass() {
        mockedCreatorTeamId = TestData.generateTestTeamId();
        mockedWinnerTeamId = TestData.generateTestTeamId();
        mockedPlayerId = Player.PlayerId.generate();
    }

    @Test
    @DisplayName("Should throw exception if player not found")
    public void should_throw_exception_if_player_not_found() {
        when(mockedPlayerReadRepository.findOneById(mockedPlayerId)).thenReturn(Optional.empty());

        assertEquals("Player not found", assertThrows(RuntimeException.class, () -> {
            processPlayerTransferUseCase.process(mockedPlayerId, BigDecimal.ONE, mockedWinnerTeamId, mockedCreatorTeamId);
        }).getMessage());
    }

    @Test
    @DisplayName("Should update the player and call CreateTransactionUseCase twice")
    public void should_update_the_player_and_call_CreateTransactionUseCase() {
        Player mockedPlayer = TestData.generateTestPlayers(mockedCreatorTeamId, 1).get(0);
        when(mockedPlayerReadRepository.findOneById(mockedPlayerId)).thenReturn(Optional.of(mockedPlayer));

        processPlayerTransferUseCase.process(mockedPlayerId, BigDecimal.TEN, mockedWinnerTeamId, mockedCreatorTeamId);

        ArgumentCaptor<Team.TeamId> teamIdCaptor = ArgumentCaptor.forClass(Team.TeamId.class);
        ArgumentCaptor<BigDecimal> amountCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        ArgumentCaptor<Transaction.TransactionType> transactionTypeCaptor = ArgumentCaptor.forClass(Transaction.TransactionType.class);

        verify(mockedCreateTransactionUseCase, times(2)).create(
                teamIdCaptor.capture(),
                amountCaptor.capture(),
                transactionTypeCaptor.capture()
        );

        assertEquals(mockedWinnerTeamId, teamIdCaptor.getAllValues().get(0));
        assertEquals(BigDecimal.TEN.negate(), amountCaptor.getAllValues().get(0));
        assertEquals(Transaction.TransactionType.PLAYER_PURCHASE, transactionTypeCaptor.getAllValues().get(0));

        assertEquals(mockedCreatorTeamId, teamIdCaptor.getAllValues().get(1));
        assertEquals(BigDecimal.TEN, amountCaptor.getAllValues().get(1));
        assertEquals(Transaction.TransactionType.PLAYER_SALE, transactionTypeCaptor.getAllValues().get(1));
    }
}