package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerCreationEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerCreationEventMongoRepository extends MongoRepository<PlayerCreationEvent, EventId> {

    Optional<PlayerCreationEvent> findByPlayerId(Player.PlayerId playerId);

}
