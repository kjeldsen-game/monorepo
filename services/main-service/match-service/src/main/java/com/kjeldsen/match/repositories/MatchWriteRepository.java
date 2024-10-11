package com.kjeldsen.match.repositories;

import com.kjeldsen.match.entities.Match;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchWriteRepository {

    Match save(Match match);
}
