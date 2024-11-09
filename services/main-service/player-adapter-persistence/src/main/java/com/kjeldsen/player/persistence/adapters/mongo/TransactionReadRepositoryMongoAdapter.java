package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.TransactionReadRepository;
import com.kjeldsen.player.domain.repositories.queries.FilterTransactionsQuery;
import com.kjeldsen.player.persistence.mongo.repositories.TransactionMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionReadRepositoryMongoAdapter implements TransactionReadRepository {

    private final TransactionMongoRepository transactionMongoRepository;

    @Override
    public List<Transaction> findAllByTeamId(Team.TeamId teamId) {
        return transactionMongoRepository.findByTeamId(teamId);
    }

    @Override
    public List<Transaction> findAllByQuery(FilterTransactionsQuery query) {
        return  transactionMongoRepository.findByTransactionQuery(query.getTransactionType(),
            query.getOccurredAt(), query.getTeamId());
    }
}