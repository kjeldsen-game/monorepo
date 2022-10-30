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
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PlayerReadRepositoryAdapter implements PlayerReadRepository {

    private final PlayerMongoRepository playerMongoRepository;

    @Override
    public Optional<Player> findOneById(PlayerId id) {
        return playerMongoRepository.findById(id.value())
                .map(PlayerDocument::toDomain);
    }

    @Override
    public List<Player> find() {
        return playerMongoRepository.findAll()
                .stream()
                .map(PlayerDocument::toDomain)
                .collect(Collectors.toList());
    }

}
