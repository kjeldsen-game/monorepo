package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerTrainingEventMongoRepository extends MongoRepository<PlayerTrainingEvent, String> {
}
