package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.TransactionEvent;

public interface TransactionEventWriteRepository {
    TransactionEvent save(TransactionEvent transactionEvent);
}