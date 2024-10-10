package com.kjeldsen.match.persistence.mongo.repositories;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.events.MatchEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchEventMongoRepository extends MongoRepository<MatchEvent, EventId> {
}