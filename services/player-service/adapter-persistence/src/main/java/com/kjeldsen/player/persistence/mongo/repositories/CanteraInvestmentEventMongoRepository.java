package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.events.domain.EventId;
import com.kjeldsen.player.domain.events.CanteraInvestmentEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CanteraInvestmentEventMongoRepository extends MongoRepository<CanteraInvestmentEvent, EventId> {

}
