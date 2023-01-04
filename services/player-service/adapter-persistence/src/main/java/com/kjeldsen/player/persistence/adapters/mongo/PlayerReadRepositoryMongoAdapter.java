package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.application.repositories.PlayerReadRepository;
import com.kjeldsen.player.application.usecases.FindPlayersQuery;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.persistence.mongo.documents.PlayerDocument;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@ConditionalOnProperty(name = "player.persistence.adapter", havingValue = "db")
@Component
@RequiredArgsConstructor
public class PlayerReadRepositoryMongoAdapter implements PlayerReadRepository {

    private final PlayerMongoRepository playerMongoRepository;

    @Override
    public Optional<Player> findOneById(PlayerId id) {
        return playerMongoRepository.findById(id.value())
            .map(PlayerDocument::toDomain);
    }

    @Override
    public List<Player> find(FindPlayersQuery query) {
        Example<PlayerDocument> playerDocumentExample = Example.of(PlayerDocument.builder()
            .position(query.getPosition() != null ? query.getPosition().name() : null)
            .build());
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize());

        return playerMongoRepository.findAll(playerDocumentExample, pageable).stream()
            .map(PlayerDocument::toDomain)
            .toList();
    }
}
