package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.persistence.mongo.documents.PlayerPositionTendencyDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerPositionTendencyMongoRepository extends MongoRepository<PlayerPositionTendencyDocument, String> {
    Optional<PlayerPositionTendencyDocument> findByPosition(PlayerPosition position);
}
