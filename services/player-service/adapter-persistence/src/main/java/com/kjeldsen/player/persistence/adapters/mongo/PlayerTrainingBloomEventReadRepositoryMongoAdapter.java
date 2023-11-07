package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;
import com.kjeldsen.player.domain.repositories.training.PlayerTrainingBloomEventReadRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerTrainingBloomEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlayerTrainingBloomEventReadRepositoryMongoAdapter implements PlayerTrainingBloomEventReadRepository {

    private final PlayerTrainingBloomEventMongoRepository playerTrainingBloomEventMongoRepository;

    @Override
    public Optional<PlayerTrainingBloomEvent> findOneByPlayerId(Player.PlayerId id) {
        return playerTrainingBloomEventMongoRepository.findOneByPlayerId(id);
    }
}
