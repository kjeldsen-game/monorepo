package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;
import com.kjeldsen.player.domain.repositories.training.PlayerTrainingBloomEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerTrainingBloomEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerTrainingBloomEventWriteRepositoryMongoAdapter implements PlayerTrainingBloomEventWriteRepository {

    final private PlayerTrainingBloomEventMongoRepository playerTrainingBloomEventMongoRepository;

    @Override
    public PlayerTrainingBloomEvent save(PlayerTrainingBloomEvent playerTrainingBloomEvent) {
        return playerTrainingBloomEventMongoRepository.save(playerTrainingBloomEvent);
    }
}
