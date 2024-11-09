package com.kjeldsen.match.repositories;

import com.kjeldsen.match.entities.Match;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MatchReadRepository {

    Optional<Match> findOneById(String id);

    List<Match> findMatchesByTeamId(String teamId);
}
