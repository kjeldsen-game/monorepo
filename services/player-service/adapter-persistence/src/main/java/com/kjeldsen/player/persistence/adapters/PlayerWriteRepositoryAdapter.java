package com.kjeldsen.player.persistence.adapters;

import com.kjeldsen.player.application.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PlayerWriteRepositoryAdapter implements PlayerWriteRepository {

    final private PlayerMongoRepository playerMongoRepository;

    @Override
    public Player save(Player player) {
        // TODO: map to document
        // PlayerDocument storedPlayer = playerMongoRepository.save(playerToStore);
        // TODO: map to domain
        return null;
    }
}
