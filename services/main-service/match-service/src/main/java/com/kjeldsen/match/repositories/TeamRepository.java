package com.kjeldsen.match.repositories;


import com.kjeldsen.match.models.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TeamRepository extends MongoRepository<Team, Long> {
    Optional<Team> findById(Long id);
}
