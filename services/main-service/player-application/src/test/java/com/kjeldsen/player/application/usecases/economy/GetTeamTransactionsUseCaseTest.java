package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.TransactionReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetTeamTransactionsUseCaseTest {

    private final TransactionReadRepository mockedTransactionReadRepository = Mockito.mock(TransactionReadRepository.class);
    private final GetTeamTransactionsUseCase getTeamTransactionsUseCase = new GetTeamTransactionsUseCase(mockedTransactionReadRepository);

    @Test
    @DisplayName("Should get Transaction summaries")
    void should_get_transaction_summaries() {
        Team.TeamId teamId = TestData.generateTestTeamId();
        List<Transaction> transactions = List.of(
          Transaction.builder()
              .transactionType(Transaction.TransactionType.SPONSOR)
              .transactionAmount(BigDecimal.valueOf(1000))
              .occurredAt(Instant.now().minus(
                  2, ChronoUnit.DAYS
              )).build(),
            Transaction.builder()
                .transactionType(Transaction.TransactionType.SPONSOR)
                .transactionAmount(BigDecimal.valueOf(1000))
                .occurredAt(Instant.now().minus(
                    10, ChronoUnit.DAYS
                )).build(),
            Transaction.builder()
                .transactionType(Transaction.TransactionType.ATTENDANCE)
                .transactionAmount(BigDecimal.valueOf(1000))
                .occurredAt(Instant.now().minus(
                    10, ChronoUnit.DAYS
                )).build()
        );

        when(mockedTransactionReadRepository.findAllByTeamId(teamId)).thenReturn(transactions);
        Map<String, GetTeamTransactionsUseCase.TeamTransactionSummary> result =
            getTeamTransactionsUseCase.getTransactions(teamId.value());
        System.out.println(result);
        assertEquals(result.get("Total Income").getSeasonSum(), BigDecimal.valueOf(3000) );
        assertEquals(result.get("Total Outcome").getSeasonSum(), BigDecimal.ZERO);
        assertEquals(result.get(Transaction.TransactionType.ATTENDANCE.name()).getSeasonSum(), BigDecimal.valueOf(1000) );
        assertEquals(result.get(Transaction.TransactionType.ATTENDANCE.name()).getWeekSum(), BigDecimal.ZERO );

        assertEquals(result.get(Transaction.TransactionType.SPONSOR.name()).getSeasonSum(), BigDecimal.valueOf(2000) );
        assertEquals(result.get(Transaction.TransactionType.SPONSOR.name()).getWeekSum(), BigDecimal.valueOf(1000) );
    }
}