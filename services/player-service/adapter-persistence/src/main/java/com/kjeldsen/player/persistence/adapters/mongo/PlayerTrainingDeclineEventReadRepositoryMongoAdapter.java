package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventReadRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerTrainingDeclineEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlayerTrainingDeclineEventReadRepositoryMongoAdapter implements PlayerTrainingDeclineEventReadRepository {

    private final PlayerTrainingDeclineEventMongoRepository playerTrainingDeclineEventMongoRepository;

    @Override
    public Optional<PlayerTrainingDeclineEvent> findOneByPlayerId(PlayerId id) {
        return playerTrainingDeclineEventMongoRepository.findOneByPlayerId(id);
    }
}
