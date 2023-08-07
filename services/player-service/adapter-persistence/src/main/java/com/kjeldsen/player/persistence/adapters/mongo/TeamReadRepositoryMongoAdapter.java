package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.persistence.mongo.repositories.TeamMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TeamReadRepositoryMongoAdapter implements TeamReadRepository {

    private final TeamMongoRepository teamMongoRepository;

    @Override
    public Optional<Team> findOneByUserId(String id) {
        return teamMongoRepository.findOneByUserId(id);
    }

}
