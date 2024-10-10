package com.kjeldsen.match.persistence.adapters.mongo;

import com.kjeldsen.match.persistence.mongo.repositories.MatchEventMongoRepository;
import com.kjeldsen.match.repositories.MatchEventWriteRepository;
import com.kjeldsen.player.domain.events.MatchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchEventWriteRepositoryMongoAdapter implements MatchEventWriteRepository {

    private final MatchEventMongoRepository matchEventMongoRepository;

    @Override
    public MatchEvent save(final MatchEvent matchEvent) {
        return matchEventMongoRepository.save(matchEvent);
    }
}