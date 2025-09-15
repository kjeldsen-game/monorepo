package com.kjeldsen.player.persistence.adapters.mongo.training.player;

import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.repositories.training.PlayerTrainingScheduledEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.training.player.PlayerTrainingScheduledEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerTrainingScheduledEventWriteRepositoryMongoAdapter implements PlayerTrainingScheduledEventWriteRepository {

    private final  PlayerTrainingScheduledEventMongoRepository playerTrainingScheduledEventMongoRepository;

    @Override
    public PlayerTrainingScheduledEvent save(PlayerTrainingScheduledEvent playerTrainingScheduledEvent) {
        return playerTrainingScheduledEventMongoRepository.save(playerTrainingScheduledEvent);
    }
}
