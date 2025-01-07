package com.kjeldsen.league.persistence.mongo.repositories;

import com.kjeldsen.league.domain.League;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueMongoRepository  extends MongoRepository<League, League.LeagueId> {
}
