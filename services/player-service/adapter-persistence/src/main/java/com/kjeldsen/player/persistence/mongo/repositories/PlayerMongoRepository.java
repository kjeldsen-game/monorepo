package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.player.domain.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerMongoRepository extends MongoRepository<Player, Player.PlayerId> {
}
