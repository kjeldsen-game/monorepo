package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerTrainingEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "player.persistence.adapter", havingValue = "db")
@Component
@RequiredArgsConstructor
public class PlayerTrainingEventWriteRepositoryMongoAdapter implements PlayerTrainingEventWriteRepository {

    final private PlayerTrainingEventMongoRepository playerTrainingEventMongoRepository;

    @Override
    public PlayerTrainingEvent save(PlayerTrainingEvent playerTrainingEvent) {
        return playerTrainingEventMongoRepository.save(playerTrainingEvent);
    }
}
