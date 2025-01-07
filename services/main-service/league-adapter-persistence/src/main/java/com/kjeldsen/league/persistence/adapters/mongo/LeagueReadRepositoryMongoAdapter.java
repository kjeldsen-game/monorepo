package com.kjeldsen.league.persistence.adapters.mongo;

import com.kjeldsen.league.domain.League;
import com.kjeldsen.league.domain.repositories.LeagueReadRepository;
import com.kjeldsen.league.persistence.mongo.repositories.LeagueMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LeagueReadRepositoryMongoAdapter implements LeagueReadRepository {

    private final LeagueMongoRepository leagueMongoRepository;

    @Override
    public Optional<League> findById(League.LeagueId leagueId) {
        return  leagueMongoRepository.findById(leagueId);
    }

    @Override
    public List<League> findAll() {
        return leagueMongoRepository.findAll();
    }
}
