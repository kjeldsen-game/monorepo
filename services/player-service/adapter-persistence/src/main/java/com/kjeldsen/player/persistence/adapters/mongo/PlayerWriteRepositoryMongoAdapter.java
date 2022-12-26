package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.application.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.persistence.mongo.documents.PlayerDocument;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "service.persistence.adapter", havingValue = "db")
@Component
@RequiredArgsConstructor
public class PlayerWriteRepositoryMongoAdapter implements PlayerWriteRepository {

    final private PlayerMongoRepository playerMongoRepository;

    @Override
    public Player save(Player player) {
        PlayerDocument playerToStore = PlayerDocument.from(player);
        return playerMongoRepository.save(playerToStore).toDomain();
    }
}
