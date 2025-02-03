package com.kjeldsen.match.persistence.mongo.repositories;

import com.kjeldsen.match.domain.entities.League;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueMongoRepository  extends MongoRepository<League, League.LeagueId> {
}