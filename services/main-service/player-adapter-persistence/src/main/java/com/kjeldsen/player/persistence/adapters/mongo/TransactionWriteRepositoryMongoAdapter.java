package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.TransactionWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.TransactionMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionWriteRepositoryMongoAdapter implements TransactionWriteRepository {

    private final TransactionMongoRepository transactionMongoRepository;

    @Override
    public Transaction save(final Transaction transaction) {
        return transactionMongoRepository.save(transaction);
    }
}