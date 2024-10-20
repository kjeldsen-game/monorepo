package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.player.domain.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMongoRepository extends MongoRepository<Team, Team.TeamId> {
    Optional<Team> findOneByUserId(String id);

    @Query(value = "{}", fields = "{ '_id' : 1 }")
    List<Team.TeamId> findAllTeamIds();
}
