package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.events.PricingEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingEventMongoRepository extends MongoRepository<PricingEvent, EventId> {
}
