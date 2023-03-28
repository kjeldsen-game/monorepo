package com.kjeldsen.player.persistence.adapters.cache;

import com.kjeldsen.player.domain.events.PlayerDeclineEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import com.kjeldsen.player.persistence.cache.PlayerTrainingDeclineEventInMemoryCacheStore;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "player.persistence.adapter", havingValue = "cache", matchIfMissing = true)
@RequiredArgsConstructor
@Component
public class PlayerTrainingDeclineEventWriteRepositoryCacheAdapter implements PlayerTrainingDeclineEventWriteRepository {

    private final PlayerTrainingDeclineEventInMemoryCacheStore playerTrainingDeclineEventInMemoryCacheStore;

    @Override
    public PlayerDeclineEvent save(PlayerDeclineEvent playerDeclineEvent) {
        playerTrainingDeclineEventInMemoryCacheStore.put(playerDeclineEvent.getPlayerId().value(), playerDeclineEvent);
        return playerDeclineEvent;
    }
}
