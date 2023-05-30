package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.events.domain.EventId;
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

}
