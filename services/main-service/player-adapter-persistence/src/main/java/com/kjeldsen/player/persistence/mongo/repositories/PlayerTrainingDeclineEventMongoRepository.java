package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.events.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerTrainingDeclineEventMongoRepository extends MongoRepository<PlayerTrainingDeclineEvent, EventId> {

    Optional<PlayerTrainingDeclineEvent> findOneByPlayerId(Player.PlayerId playerId);

}
