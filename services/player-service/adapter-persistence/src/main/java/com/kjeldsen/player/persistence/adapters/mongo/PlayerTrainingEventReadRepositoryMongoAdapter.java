package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventReadRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerTrainingEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PlayerTrainingEventReadRepositoryMongoAdapter implements PlayerTrainingEventReadRepository {

    private final PlayerTrainingEventMongoRepository playerTrainingEventMongoRepository;

    @Override
    public List<PlayerTrainingEvent> findAllByPlayerId(PlayerId playerId) {
        return playerTrainingEventMongoRepository.findAllByPlayerId(playerId);
    }
}
