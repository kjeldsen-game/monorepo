package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PlayerPotentialRiseEventMongoRepository extends MongoRepository<PlayerPotentialRiseEvent, EventId> {
    List<PlayerPotentialRiseEvent> findAllByPlayerId(Player.PlayerId playerId);
}
