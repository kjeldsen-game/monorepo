package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TransactionReadRepository;
import com.kjeldsen.player.domain.repositories.queries.FilterTransactionsQuery;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetPlayerWagesTransactionsUseCase {

    private static final int KJELDSEN_WEEK_LENGTH = 4;
    private static final int KJELDSEN_SEASON_LENGTH = 90;
    private final PlayerReadRepository playerReadRepository;
    private final TransactionReadRepository transactionReadRepository;


    public List<PlayerWageSummary> getPlayerWagesTransactions(final String teamId) {
        List<PlayerWageSummary> wageSummaryList = new ArrayList<>();
        Instant now = Instant.now();
        Instant fourDaysAgo = now.minus(KJELDSEN_WEEK_LENGTH, ChronoUnit.DAYS);
        Instant ninetyDaysAgo = now.minus(KJELDSEN_SEASON_LENGTH, ChronoUnit.DAYS);

        List<Transaction> wagesTransactions = transactionReadRepository.findAllByQuery(
            FilterTransactionsQuery.builder()
                .transactionType(Transaction.TransactionType.PLAYER_WAGE)
                .teamId(Team.TeamId.of(teamId))
                .occurredAt(ninetyDaysAgo)
                .build());

        List<Player> teamPlayers = playerReadRepository.findByTeamId(Team.TeamId.of(teamId));
        Set<String> playerIds = teamPlayers.stream().map(
            player -> player.getId().value()).collect(Collectors.toSet());

        List<Transaction> filteredWagesTransactions = wagesTransactions.stream()
            .filter(transaction -> playerIds.contains(transaction.getMessage()))
            .toList();

        Map<Player, List<Transaction>> playerTransactionsMap = new HashMap<>();
        for (Player player : teamPlayers) {
            List<Transaction> playerTransactions = filteredWagesTransactions.stream()
                .filter(transaction -> transaction.getMessage().equals(player.getId().value()))
                .collect(Collectors.toList());

            if (!playerTransactions.isEmpty()) {
                playerTransactionsMap.put(player, playerTransactions);
            }
        }

        for (Map.Entry<Player, List<Transaction>> entry : playerTransactionsMap.entrySet()) {
            Player player = entry.getKey();
            List<Transaction> playerTransactions = entry.getValue();

            BigDecimal weekSum = typeAmountPerPeriods(playerTransactions, t -> t.getOccurredAt().isAfter(fourDaysAgo));
            BigDecimal seasonSum = typeAmountPerPeriods(playerTransactions, t -> t.getOccurredAt().isAfter(ninetyDaysAgo));

            wageSummaryList.add(PlayerWageSummary.builder()
                .player(Player.builder()
                    .id(player.getId())
                    .name(player.getName())
                    .age(player.getAge())
                    .status(player.getStatus())
                    .position(player.getPosition())
                    .build())
                .transactionSummary(new TransactionSummary(weekSum, seasonSum))
                .build());
        }
        return wageSummaryList;
    }

    private BigDecimal typeAmountPerPeriods(List<Transaction> transactions, Predicate<Transaction> filterCondition ) {
        return transactions.stream()
            .filter(filterCondition)
            .map(Transaction::getTransactionAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Getter
    @Setter
    @Builder
    public static class PlayerWageSummary {
        Player player;
        TransactionSummary transactionSummary;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class TransactionSummary {
        private BigDecimal weekSummary = BigDecimal.ZERO;
        private BigDecimal seasonSummary = BigDecimal.ZERO;
    }
}
