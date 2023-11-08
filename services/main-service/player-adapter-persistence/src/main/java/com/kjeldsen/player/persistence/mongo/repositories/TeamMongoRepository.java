package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.player.domain.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamMongoRepository extends MongoRepository<Team, Team.TeamId> {
    Optional<Team> findOneByUserId(String id);
}
