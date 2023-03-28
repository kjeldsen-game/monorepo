package com.kjeldsen.player.persistence.adapters.cache;

import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventWriteRepository;
import com.kjeldsen.player.persistence.cache.PlayerTrainingEventInMemoryCacheStore;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "player.persistence.adapter", havingValue = "cache", matchIfMissing = true)
@RequiredArgsConstructor
@Component
public class PlayerTrainingEventWriteRepositoryCacheAdapter implements PlayerTrainingEventWriteRepository {

    private final PlayerTrainingEventInMemoryCacheStore playerTrainingEventInMemoryCacheStore;

    @Override
    public PlayerTrainingEvent save(PlayerTrainingEvent playerTrainingEvent) {
        playerTrainingEventInMemoryCacheStore.put(playerTrainingEvent.getPlayerId().value(), playerTrainingEvent);
        return playerTrainingEvent;
    }
}
