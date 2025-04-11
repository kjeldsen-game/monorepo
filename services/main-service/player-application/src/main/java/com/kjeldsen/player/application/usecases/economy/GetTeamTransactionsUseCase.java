package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.TransactionReadRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

// TODO add unit testing

@Component
@RequiredArgsConstructor
@Slf4j
public class GetTeamTransactionsUseCase extends GetTransactionsUseCaseAbstract {

    private static final int KJELDSEN_WEEK_LENGTH = 4;
    private static final int KJELDSEN_SEASON_LENGTH = 90;
    private static final List<Transaction.TransactionType> INCOME_TRANSACTION_TYPES = List.of(Transaction.TransactionType.SPONSOR,
        Transaction.TransactionType.PLAYER_SALE, Transaction.TransactionType.ATTENDANCE, Transaction.TransactionType.MERCHANDISE,
        Transaction.TransactionType.RESTAURANT, Transaction.TransactionType.BILLBOARDS);
    private final TransactionReadRepository transactionReadRepository;

    public Map<String, TransactionSummary> getTransactions(final String teamId) {
        // Add filter probably only last 90 days
        Map<String, TransactionSummary> totalSummary = new LinkedHashMap<>() {{
            put("Total Income", new TransactionSummary());
            put("Total Outcome", new TransactionSummary());
            put("Total Balance", new TransactionSummary());
        }};

        List<Transaction> transactions = transactionReadRepository.findAllByTeamId(Team.TeamId.of(teamId));
        Map<String, TransactionSummary> result = new LinkedHashMap<>();

        Instant now = Instant.now();
        Instant fourDaysAgo = now.minus(KJELDSEN_WEEK_LENGTH, ChronoUnit.DAYS);
        Instant ninetyDaysAgo = now.minus(KJELDSEN_SEASON_LENGTH, ChronoUnit.DAYS);

        for (Transaction.TransactionType type : Transaction.TransactionType.values()) {
            result.put(type.name(), new TransactionSummary());
        }

        Map<Transaction.TransactionType, List<Transaction>> groupedTransactions = transactions.stream()
            .collect(Collectors.groupingBy(Transaction::getTransactionType));

        for (Map.Entry<Transaction.TransactionType, List<Transaction>> entry : groupedTransactions.entrySet()) {
            Transaction.TransactionType transactionType = entry.getKey();
            List<Transaction> typeTransactions = entry.getValue();

            BigDecimal weekSum = typeAmountPerPeriods(typeTransactions, t -> t.getOccurredAt().isAfter(fourDaysAgo));
            BigDecimal seasonSum = typeAmountPerPeriods(typeTransactions, t -> t.getOccurredAt().isAfter(ninetyDaysAgo));

            TransactionSummary transactionTypeSummary = new TransactionSummary(weekSum, seasonSum);

            filterTotal(totalSummary, transactionTypeSummary, transactionType);
            result.put(transactionType.name(), transactionTypeSummary);
        }

        // Update the Map w Totals
        result.putAll(totalSummary);
        return result;
    }

    private void filterTotal(Map<String, TransactionSummary> totalSummaryMap, TransactionSummary typeSummary, Transaction.TransactionType transactionType) {
        // Update the Total Income/Outcome
        TransactionSummary totalSum;
        if (INCOME_TRANSACTION_TYPES.contains(transactionType)) {
            totalSum = totalSummaryMap.get("Total Income");
        } else {
            totalSum = totalSummaryMap.get("Total Outcome");
        }

        totalSum.setWeekSummary(totalSum.getWeekSummary().add(typeSummary.getWeekSummary()));
        totalSum.setSeasonSummary(totalSum.getSeasonSummary().add(typeSummary.getSeasonSummary()));

        // Update the Total Balance
        TransactionSummary totalBalance = totalSummaryMap.get("Total Balance");
        totalBalance.setSeasonSummary(totalBalance.getSeasonSummary().add(typeSummary.getSeasonSummary()));
        totalBalance.setWeekSummary(totalBalance.getWeekSummary().add(typeSummary.getWeekSummary()));
    }
}
