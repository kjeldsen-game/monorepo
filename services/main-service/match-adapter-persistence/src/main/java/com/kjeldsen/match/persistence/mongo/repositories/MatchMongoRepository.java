package com.kjeldsen.match.persistence.mongo.repositories;

import com.kjeldsen.match.domain.entities.Match;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchMongoRepository extends MongoRepository<Match, String> {

    @Query("{$or: [{'home.id': ?0}, {'away.id': ?0}]}")
    List<Match> findMatchesByTeamId(String teamId);

    @Query("{'leagueId': ?0}")
    List<Match> findMatchesByLeagueId(String leagueId);


    @Query("{ 'leagueId': ?0, 'status': ?1, $or: [ {'home._id': ?2}, {'away._id': ?2} ] }")
    List<Match> findMatchesByLeagueIdStatusAndTeamId(String leagueId, Match.Status status, String teamId);
}


