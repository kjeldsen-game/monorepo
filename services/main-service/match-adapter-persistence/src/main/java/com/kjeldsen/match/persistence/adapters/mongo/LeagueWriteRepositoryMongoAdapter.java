package com.kjeldsen.match.persistence.adapters.mongo;

import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.repositories.LeagueWriteRepository;
import com.kjeldsen.match.persistence.mongo.repositories.LeagueMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeagueWriteRepositoryMongoAdapter implements LeagueWriteRepository {

    private final LeagueMongoRepository leagueMongoRepository;

    @Override
    public League save(League league) {
        return leagueMongoRepository.save(league);
    }
}

