package com.kjeldsen.match.rest.delegate;

import com.kjeldsen.match.models.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends MongoRepository<Team, Long> {

}
