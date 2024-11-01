package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TransactionMongoRepository extends MongoRepository<Transaction, Transaction.TransactionId> {
    List<Transaction> findByTeamId(Team.TeamId teamId);

    @Query("{ 'transactionType': ?0, 'occurredAt': { $gte: ?1 }, 'teamId': ?2 }")
    List<Transaction> findByTransactionQuery(Transaction.TransactionType transactionType, Instant occurredAt, Team.TeamId teamId);
}
