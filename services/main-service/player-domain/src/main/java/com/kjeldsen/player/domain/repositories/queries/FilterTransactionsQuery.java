package com.kjeldsen.player.domain.repositories.queries;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class FilterTransactionsQuery {
    Team.TeamId teamId;
    Transaction.TransactionType transactionType;
    Instant occurredAt;
}
