package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.Transaction;

public interface TransactionWriteRepository {

    Transaction save(Transaction transaction);
}
