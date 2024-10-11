package com.kjeldsen.match.persistence.adapters.mongo;

import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.persistence.mongo.repositories.MatchMongoRepository;
import com.kjeldsen.match.repositories.MatchWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchWriteRepositoryMongoAdapter implements MatchWriteRepository {

    private final MatchMongoRepository matchMongoRepository;

    @Override
    public Match save(Match match) {
        return matchMongoRepository.save(match);
    }
}
