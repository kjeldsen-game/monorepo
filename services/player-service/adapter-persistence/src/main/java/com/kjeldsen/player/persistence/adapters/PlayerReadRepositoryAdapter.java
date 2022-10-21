package com.kjeldsen.player.persistence.adapters;

import com.kjeldsen.player.application.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.persistence.mongo.documents.PlayerDocument;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class PlayerReadRepositoryAdapter implements PlayerReadRepository {

    private final PlayerMongoRepository playerMongoRepository;

    @Override
    public Optional<Player> findOneById(PlayerId id) {
        Optional<PlayerDocument> storedPlayer = playerMongoRepository.findById(id.value());
        // TODO: map to domain
        return null;
    }

    @Override
    public List<Player> find() {
        List<PlayerDocument> storedPlayers = playerMongoRepository.findAll();
        // TODO: map to domain
        return null;
    }

}
