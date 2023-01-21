package com.kjeldsen.player.persistence.adapters.cache;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.persistence.cache.PlayerInMemoryCacheStore;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "player.persistence.adapter", havingValue = "cache", matchIfMissing = true)
@RequiredArgsConstructor
@Component
public class PlayerWriteRepositoryCacheAdapter implements PlayerWriteRepository {

    private final PlayerInMemoryCacheStore playerStore;

    @Override
    public Player save(Player player) {
        playerStore.put(player.getId().value(), player);
        return player;
    }
}
