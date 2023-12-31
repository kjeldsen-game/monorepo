package com.kjeldsen.match.rest.delegate;

import com.kjeldsen.match.entities.Match;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends MongoRepository<Match, String> {

    Optional<Match> findOneById(String id);

    @Query("{$or: [{'home.id': ?0}, {'away.id': ?0}]}")
    List<Match> findMatchesByTeamId(String teamId);
}
