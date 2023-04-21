package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerWriteRepositoryMongoAdapter implements PlayerWriteRepository {

    private final PlayerMongoRepository playerMongoRepository;

    @Override
    public Player save(Player player) {
        return playerMongoRepository.save(player);
    }
}

