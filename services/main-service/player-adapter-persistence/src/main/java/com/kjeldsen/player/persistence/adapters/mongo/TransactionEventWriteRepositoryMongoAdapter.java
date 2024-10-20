package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.events.TransactionEvent;
import com.kjeldsen.player.domain.repositories.TransactionEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.TransactionEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionEventWriteRepositoryMongoAdapter implements TransactionEventWriteRepository {
    private final TransactionEventMongoRepository transactionEventMongoRepository;

    @Override
    public TransactionEvent save(TransactionEvent transactionEvent) {
        return transactionEventMongoRepository.save(transactionEvent);
    }
}