package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.events.PlayerCreationEvent;
import com.kjeldsen.player.domain.repositories.player.PlayerCreationEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerCreationEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerCreationEventWriteRepositoryMongoAdapter implements PlayerCreationEventWriteRepository {

    private final PlayerCreationEventMongoRepository playerCreationEventMongoRepository;

    @Override
    public PlayerCreationEvent save(PlayerCreationEvent playerCreationEvent) {
        return playerCreationEventMongoRepository.save(playerCreationEvent);
    }
}
