package com.kjeldsen.match.persistence.mongo.repositories;

import com.kjeldsen.match.entities.Match;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchMongoRepository extends MongoRepository<Match, String> {

    @Query("{$or: [{'home.id': ?0}, {'away.id': ?0}]}")
    List<Match> findMatchesByTeamId(String teamId);
}
