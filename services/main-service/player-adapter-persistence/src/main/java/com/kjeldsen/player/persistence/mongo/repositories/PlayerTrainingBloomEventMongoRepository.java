package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerTrainingBloomEventMongoRepository extends MongoRepository<PlayerTrainingBloomEvent, EventId> {

    Optional<PlayerTrainingBloomEvent> findOneByPlayerId(Player.PlayerId playerId);

}
