package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerMongoRepository extends MongoRepository<Player, PlayerId> {
}
