package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerTrainingEventMongoRepository extends MongoRepository<PlayerTrainingEvent, EventId> {

    List<PlayerTrainingEvent> findAllByPlayerId(Player.PlayerId playerId);

    @Query(value = "{ 'scheduledTrainingId': ?0 }", sort = "{ 'occurredAt': -1 }")
    Page<PlayerTrainingEvent> findLatestByPlayerTrainingScheduledEventId(String playerTrainingScheduledEventId,
                                                                         PageRequest pageRequest);

    @Query("{ 'playerId': ?0,'teamId':  ?1,  $expr: { $ne: ['$pointsBeforeTraining', '$pointsAfterTraining'] } }")
    List<PlayerTrainingEvent> findAllSuccessfulByPlayerId(Player.PlayerId playerId, Team.TeamId teamId);

}
