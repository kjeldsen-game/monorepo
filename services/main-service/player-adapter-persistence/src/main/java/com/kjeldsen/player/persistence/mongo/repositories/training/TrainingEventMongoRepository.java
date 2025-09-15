package com.kjeldsen.player.persistence.mongo.repositories.training;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.models.training.TrainingEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingEventMongoRepository extends MongoRepository<TrainingEvent, String> {

//    @Query(value = "{ 'playerId': ?0, 'type': ?1 }", sort = "{ 'occurredAt': -1 }")
//    Optional<TrainingEvent> findLatestByPlayerIdAndType(Player.PlayerId playerId, TrainingEvent.TrainingType type);

    Optional<TrainingEvent> findFirstByPlayerIdAndTypeOrderByOccurredAtDesc(Player.PlayerId playerId, TrainingEvent.TrainingType type);

    Optional<TrainingEvent> findFirstByReferenceOrderByOccurredAtDesc(String reference);
}
