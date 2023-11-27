package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseScheduledEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface PlayerPotentialRiseScheduledEventMongoRepository extends MongoRepository<PlayerPotentialRiseScheduledEvent, EventId> {
    @Query(value = "from PlayerPotentialRiseScheduledEvent t where :date BETWEEN startDate AND endDate")
    List<PlayerPotentialRiseScheduledEvent> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate startDate);
}
