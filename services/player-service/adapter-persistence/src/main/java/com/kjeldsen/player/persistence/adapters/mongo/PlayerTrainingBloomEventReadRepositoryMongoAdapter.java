package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingBloomEventReadRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerTrainingBloomEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlayerTrainingBloomEventReadRepositoryMongoAdapter implements PlayerTrainingBloomEventReadRepository {

    private final PlayerTrainingBloomEventMongoRepository playerTrainingBloomEventMongoRepository;

    @Override
    public Optional<PlayerTrainingBloomEvent> findOneByPlayerId(PlayerId id) {
        return playerTrainingBloomEventMongoRepository.findById(id.value());
    }
}
