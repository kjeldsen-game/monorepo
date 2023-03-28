package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.player.domain.events.PlayerDeclineEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerTrainingDeclineEventMongoRepository extends MongoRepository<PlayerDeclineEvent, String> {
}
