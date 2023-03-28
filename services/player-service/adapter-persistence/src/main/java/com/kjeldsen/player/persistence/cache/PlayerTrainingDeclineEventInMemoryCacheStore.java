package com.kjeldsen.player.persistence.cache;

import com.kjeldsen.player.domain.events.PlayerDeclineEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "player.persistence.adapter", havingValue = "cache", matchIfMissing = true)
@Component
public class PlayerTrainingDeclineEventInMemoryCacheStore extends InMemoryCacheStore<String, PlayerDeclineEvent> {

    public PlayerTrainingDeclineEventInMemoryCacheStore(
        @Value("${service.persistence.cache.ttl:60000}") int timeToLive,
        @Value("${service.persistence.cache.size:1000}") int cacheSize,
        @Value("${service.persistence.cache.cleanup-interval:1000}") int cleanUpInterval) {
        super(timeToLive, cacheSize, cleanUpInterval);
    }
}
