package com.kjeldsen.match.persistence.adapters.mongo;

import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.persistence.mongo.repositories.MatchEventMongoRepository;
import com.kjeldsen.match.persistence.mongo.repositories.MatchMongoRepository;
import com.kjeldsen.match.repositories.MatchReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MatchReadRepositoryMongoAdapter implements MatchReadRepository {

    private final MatchMongoRepository matchMongoRepository;

    @Override
    public Optional<Match> findOneById(String id) {
        return matchMongoRepository.findById(id);
    }

    @Override
    public List<Match> findMatchesByTeamId(String teamId) {
        return matchMongoRepository.findMatchesByTeamId(teamId);
    }
}