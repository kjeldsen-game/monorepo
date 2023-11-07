package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.repositories.training.PlayerTrainingEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerTrainingEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerTrainingEventWriteRepositoryMongoAdapter implements PlayerTrainingEventWriteRepository {

    final private PlayerTrainingEventMongoRepository playerTrainingEventMongoRepository;

    @Override
    public PlayerTrainingEvent save(PlayerTrainingEvent playerTrainingEvent) {
        return playerTrainingEventMongoRepository.save(playerTrainingEvent);
    }
}
