package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.TransactionReadRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.data.MapEntry;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// TODO add unit testing

@Component
@RequiredArgsConstructor
@Slf4j
public class GetTeamTransactionsUseCase {

    private static final int KJELDSEN_WEEK_LENGTH = 4;
    private static final int KJELDSEN_SEASON_LENGTH = 90;
    private static final List<Transaction.TransactionType> INCOME_TRANSACTION_TYPES = List.of(Transaction.TransactionType.SPONSOR,
        Transaction.TransactionType.PLAYER_SALE, Transaction.TransactionType.ATTENDANCE, Transaction.TransactionType.MERCHANDISE,
        Transaction.TransactionType.RESTAURANT, Transaction.TransactionType.BILLBOARDS);
    private final TransactionReadRepository transactionReadRepository;

    public Map<String, TeamTransactionSummary> getTransactions(final String teamId) {
        // Add filter probably only last 90 days
        Map<String, TeamTransactionSummary> totalSummary = new LinkedHashMap<>() {{
            put("Total Income", new TeamTransactionSummary());
            put("Total Outcome", new TeamTransactionSummary());
            put("Total Balance", new TeamTransactionSummary());
        }};

        List<Transaction> transactions = transactionReadRepository.findAllByTeamId(Team.TeamId.of(teamId));
        Map<String, TeamTransactionSummary> result = new LinkedHashMap<>();

        Instant now = Instant.now();
        Instant fourDaysAgo = now.minus(KJELDSEN_WEEK_LENGTH, ChronoUnit.DAYS);
        Instant eightDaysAgo = now.minus(KJELDSEN_WEEK_LENGTH * 2, ChronoUnit.DAYS);
        Instant ninetyDaysAgo = now.minus(KJELDSEN_SEASON_LENGTH, ChronoUnit.DAYS);

        for (Transaction.TransactionType type : Transaction.TransactionType.values()) {
            result.put(type.name(), new TeamTransactionSummary(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        }

        Map<Transaction.TransactionType, List<Transaction>> groupedTransactions = transactions.stream()
            .collect(Collectors.groupingBy(Transaction::getTransactionType));

        for (Map.Entry<Transaction.TransactionType, List<Transaction>> entry : groupedTransactions.entrySet()) {
            Transaction.TransactionType transactionType = entry.getKey();
            List<Transaction> typeTransactions = entry.getValue();

            BigDecimal weekSum = typeAmountPerPeriods(typeTransactions, t -> t.getOccurredAt().isAfter(fourDaysAgo));
            BigDecimal lastWeekSum = typeAmountPerPeriods(typeTransactions, t -> t.getOccurredAt().isBefore(fourDaysAgo) &&
                t.getOccurredAt().isAfter(eightDaysAgo));
            BigDecimal seasonSum = typeAmountPerPeriods(typeTransactions, t -> t.getOccurredAt().isAfter(ninetyDaysAgo));

            TeamTransactionSummary transactionTypeSummary = new TeamTransactionSummary(weekSum, lastWeekSum, seasonSum);
            filterTotal(totalSummary, transactionTypeSummary, transactionType);
            result.put(transactionType.name(), transactionTypeSummary);
        }

        // Update the Map w Totals
        result.putAll(totalSummary);
        return result;
    }

    private BigDecimal typeAmountPerPeriods(List<Transaction> transactions, Predicate<Transaction> filterCondition ) {
        return transactions.stream()
            .filter(filterCondition)
            .map(Transaction::getTransactionAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class TeamTransactionSummary {
        private BigDecimal weekSum = BigDecimal.ZERO;
        private BigDecimal lastWeekSum = BigDecimal.ZERO;
        private BigDecimal seasonSum = BigDecimal.ZERO;
    }

    private void filterTotal(Map<String, TeamTransactionSummary> totalSummaryMap, TeamTransactionSummary typeSummary, Transaction.TransactionType transactionType) {
        // Update the Total Income/Outcome
        TeamTransactionSummary totalSum;
        if (INCOME_TRANSACTION_TYPES.contains(transactionType)) {
            totalSum = totalSummaryMap.get("Total Income");
        } else {
            totalSum = totalSummaryMap.get("Total Outcome");
        }
        totalSum.weekSum = totalSum.weekSum.add(typeSummary.weekSum);
        totalSum.lastWeekSum = totalSum.lastWeekSum.add(typeSummary.lastWeekSum);
        totalSum.seasonSum = totalSum.seasonSum.add(typeSummary.seasonSum);

        // Update the Total Balance
        TeamTransactionSummary totalBalance = totalSummaryMap.get("Total Balance");
        totalBalance.weekSum = totalBalance.weekSum.add(typeSummary.weekSum);
        totalBalance.lastWeekSum = totalBalance.lastWeekSum.add(typeSummary.lastWeekSum);
        totalBalance.seasonSum = totalBalance.seasonSum.add(typeSummary.seasonSum);
    }
}
