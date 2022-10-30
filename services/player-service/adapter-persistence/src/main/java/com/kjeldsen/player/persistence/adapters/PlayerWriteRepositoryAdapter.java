package com.kjeldsen.player.persistence.adapters;

import com.kjeldsen.player.application.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.persistence.mongo.documents.PlayerDocument;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PlayerWriteRepositoryAdapter implements PlayerWriteRepository {

    final private PlayerMongoRepository playerMongoRepository;

    @Override
    public Player save(Player player) {
        PlayerDocument playerToStore = PlayerDocument.from(player);
        return playerMongoRepository.save(playerToStore).toDomain();
    }
}
