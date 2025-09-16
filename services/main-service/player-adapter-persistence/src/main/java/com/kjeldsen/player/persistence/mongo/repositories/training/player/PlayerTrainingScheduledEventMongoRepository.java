package com.kjeldsen.player.persistence.mongo.repositories.training.player;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlayerTrainingScheduledEventMongoRepository extends MongoRepository<PlayerTrainingScheduledEvent, EventId> {

    @Query(value = "from PlayerTrainingScheduledEvent t where :date BETWEEN startDate AND endDate")
    List<PlayerTrainingScheduledEvent> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate startDate);

    @Query("{ 'status': 'ACTIVE' }")
    List<PlayerTrainingScheduledEvent> findAllWhereStatusIsActive();

    List<PlayerTrainingScheduledEvent> findAllByPlayerId(Player.PlayerId playerId);
}
