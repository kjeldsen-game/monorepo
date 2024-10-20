package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionMongoRepository extends MongoRepository<Transaction, Transaction.TransactionId> {
    List<Transaction> findByTeamId(Team.TeamId teamId);
}
