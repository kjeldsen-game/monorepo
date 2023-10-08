package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.events.domain.EventId;
import com.kjeldsen.player.domain.events.ExpenseEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseEventMongoRepository extends MongoRepository<ExpenseEvent, EventId> {

}
