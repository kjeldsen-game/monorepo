package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerCreationEvent;
import com.kjeldsen.player.domain.repositories.player.PlayerCreationEventReadRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerCreationEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlayerCreationEventReadRepositoryMongoAdapter implements PlayerCreationEventReadRepository {

    private final PlayerCreationEventMongoRepository playerCreationEventMongoRepository;

    @Override
    public Optional<PlayerCreationEvent> findByPlayerId(Player.PlayerId playerId) {
        return playerCreationEventMongoRepository.findByPlayerId(playerId);
    }
}
