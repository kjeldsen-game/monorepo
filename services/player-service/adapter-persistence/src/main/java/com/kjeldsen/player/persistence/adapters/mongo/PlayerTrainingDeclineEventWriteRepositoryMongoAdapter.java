package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerTrainingDeclineEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerTrainingDeclineEventWriteRepositoryMongoAdapter implements PlayerTrainingDeclineEventWriteRepository {

    private final PlayerTrainingDeclineEventMongoRepository playerTrainingDeclineEventMongoRepository;

    @Override
    public PlayerTrainingDeclineEvent save(PlayerTrainingDeclineEvent playerTrainingDeclineEvent) {
        return playerTrainingDeclineEventMongoRepository.save(playerTrainingDeclineEvent);
    }
}
