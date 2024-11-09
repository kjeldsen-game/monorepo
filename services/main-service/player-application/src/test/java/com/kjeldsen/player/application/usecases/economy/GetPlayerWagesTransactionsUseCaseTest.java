package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.TransactionReadRepository;
import com.kjeldsen.player.domain.repositories.queries.FilterTransactionsQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class GetPlayerWagesTransactionsUseCaseTest {

    private final PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final TransactionReadRepository mockedTransactionReadRepository = Mockito.mock(TransactionReadRepository.class);
    private final GetPlayerWagesTransactionsUseCase getPlayerWagesTransactionsUseCase = new GetPlayerWagesTransactionsUseCase(
        mockedPlayerReadRepository, mockedTransactionReadRepository);

    @Test
    @DisplayName("Should get player wages transactions summary")
    public void should_get_player_wages_transactions_summary() {
        Team.TeamId teamId = TestData.generateTestTeamId();
        List<Player> players = TestData.generateTestPlayers(teamId, 5);
        List<Transaction> transactions = List.of(
            Transaction.builder().transactionAmount(BigDecimal.TEN).transactionType(
                Transaction.TransactionType.PLAYER_WAGE).occurredAt(Instant.now().minus(1, ChronoUnit.DAYS)).message(players.get(0).getId().value()).build(),
            Transaction.builder().transactionAmount(BigDecimal.TEN).transactionType(
                Transaction.TransactionType.PLAYER_WAGE).occurredAt(Instant.now().minus(10, ChronoUnit.DAYS)).message(players.get(1).getId().value()).build(),
            Transaction.builder().transactionAmount(BigDecimal.valueOf(1000)).transactionType(
                Transaction.TransactionType.PLAYER_WAGE).occurredAt(Instant.now().minus(10, ChronoUnit.DAYS)).message(players.get(1).getId().value()).build()
            );

        when(mockedPlayerReadRepository.findByTeamId(teamId)).thenReturn(players);
        when(mockedTransactionReadRepository.findAllByQuery(any()))
            .thenReturn(transactions);

        List<GetPlayerWagesTransactionsUseCase.PlayerWageSummary> result =
            getPlayerWagesTransactionsUseCase.getPlayerWagesTransactions(teamId.value());

        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(playerWageSummary ->
            playerWageSummary.getPlayer().getId().equals(players.get(1).getId()) &&
                playerWageSummary.getTransactionSummary().getSeasonSummary().compareTo(BigDecimal.valueOf(1010)) == 0));
        assertTrue(result.stream().anyMatch(playerWageSummary ->
            playerWageSummary.getPlayer().getId().equals(players.get(0).getId()) &&
                playerWageSummary.getTransactionSummary().getWeekSummary().compareTo(BigDecimal.valueOf(10)) == 0));
    }
}