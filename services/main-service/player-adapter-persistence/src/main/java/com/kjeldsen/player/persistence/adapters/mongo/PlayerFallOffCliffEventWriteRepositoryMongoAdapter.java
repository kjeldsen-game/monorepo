package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.events.PlayerFallOffCliffEvent;
import com.kjeldsen.player.domain.repositories.PlayerFallOffCliffEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerFallOffCliffEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerFallOffCliffEventWriteRepositoryMongoAdapter implements PlayerFallOffCliffEventWriteRepository {

    private final PlayerFallOffCliffEventMongoRepository playerFallOffCliffEventMongoRepository;

    @Override
    public PlayerFallOffCliffEvent save(PlayerFallOffCliffEvent playerFallOffCliffEvent) {
        return playerFallOffCliffEventMongoRepository.save(playerFallOffCliffEvent);
    }
}
