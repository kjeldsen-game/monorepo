package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.queries.FilterTransactionsQuery;

import java.util.List;

public interface TransactionReadRepository {

    List<Transaction> findAllByTeamId(Team.TeamId teamId);

    List<Transaction> findAllByQuery(FilterTransactionsQuery query);
}
