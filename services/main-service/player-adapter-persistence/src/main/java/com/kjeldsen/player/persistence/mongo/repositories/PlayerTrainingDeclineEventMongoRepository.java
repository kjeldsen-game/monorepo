package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerTrainingDeclineEventMongoRepository extends MongoRepository<PlayerTrainingDeclineEvent, EventId> {

    Optional<PlayerTrainingDeclineEvent> findOneByPlayerId(Player.PlayerId playerId);

    @Query(value = "{ 'playerId': ?0 }", sort = "{ 'occurredAt': -1 }")
    Page<PlayerTrainingDeclineEvent> findLatestByPlayerId(Player.PlayerId playerId, PageRequest pageRequest);
}
