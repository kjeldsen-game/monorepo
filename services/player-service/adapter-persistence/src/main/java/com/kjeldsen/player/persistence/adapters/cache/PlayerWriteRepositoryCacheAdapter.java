package com.kjeldsen.player.persistence.adapters.cache;

import com.kjeldsen.player.application.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.persistence.cache.PlayerInMemoryCacheStore;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "service.persistence.adapter", havingValue = "cache", matchIfMissing = true)
@AllArgsConstructor
@Component
public class PlayerWriteRepositoryCacheAdapter implements PlayerWriteRepository {

    private final PlayerInMemoryCacheStore playerStore;

    @Override
    public Player save(Player player) {
        playerStore.put(player.getId().value(), player);
        return player;
    }
}
