package com.kjeldsen.league.persistence.adapters.mongo;

import com.kjeldsen.league.domain.League;
import com.kjeldsen.league.domain.repositories.LeagueWriteRepository;
import com.kjeldsen.league.persistence.mongo.repositories.LeagueMongoRepository;
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
