package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerPositionTendencyMongoRepository extends MongoRepository<PlayerPositionTendency, String> {
    Optional<PlayerPositionTendency> findByPosition(PlayerPosition position);
}
