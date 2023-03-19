package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.events.EventId;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerTrainingBloomEventMongoRepository extends MongoRepository<PlayerTrainingBloomEvent, EventId> {

    Optional<PlayerTrainingBloomEvent> findOneByPlayerId(PlayerId playerId);

}
