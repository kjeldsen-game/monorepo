package com.kjeldsen.match.persistence.adapters.mongo;

import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.repositories.LeagueReadRepository;
import com.kjeldsen.match.persistence.mongo.repositories.LeagueMongoRepository;
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

    @Override
    public List<League> findAllByStatus(League.LeagueStatus status) {
        return leagueMongoRepository.findAllByStatus(status);
    }
}
