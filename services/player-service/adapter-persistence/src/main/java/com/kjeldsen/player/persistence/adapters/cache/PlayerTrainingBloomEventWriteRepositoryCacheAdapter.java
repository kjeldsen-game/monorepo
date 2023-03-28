package com.kjeldsen.player.persistence.adapters.cache;

import com.kjeldsen.player.domain.events.PlayerBloomEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingBloomEventWriteRepository;
import com.kjeldsen.player.persistence.cache.PlayerTrainingBloomEventInMemoryCacheStore;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "player.persistence.adapter", havingValue = "cache", matchIfMissing = true)
@RequiredArgsConstructor
@Component
public class PlayerTrainingBloomEventWriteRepositoryCacheAdapter implements PlayerTrainingBloomEventWriteRepository {

    private final PlayerTrainingBloomEventInMemoryCacheStore playerTrainingBloomEventInMemoryCacheStore;

    @Override
    public PlayerBloomEvent save(PlayerBloomEvent playerBloomEvent) {
        playerTrainingBloomEventInMemoryCacheStore.put(playerBloomEvent.getPlayerId().value(), playerBloomEvent);
        return playerBloomEvent;
    }
}
