package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.application.repositories.PlayerPositionTendencyReadRepository;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.persistence.mongo.documents.PlayerPositionTendencyDocument;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerPositionTendencyMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlayerPositionTendencyReadRepositoryMongoAdapter implements PlayerPositionTendencyReadRepository {

    private final PlayerPositionTendencyMongoRepository playerPositionTendencyMongoRepository;

    @Override
    public PlayerPositionTendency get(PlayerPosition position) {
        Optional<PlayerPositionTendencyDocument> storedPlayerPositionTendency = playerPositionTendencyMongoRepository.findByPosition(position);

        return storedPlayerPositionTendency.map(PlayerPositionTendencyDocument::toDomain)
            .orElse(PlayerPositionTendency.getDefault(position));
    }

    @Override
    public List<PlayerPositionTendency> find() {
        List<PlayerPositionTendency> storedPlayerPositionTendencies = playerPositionTendencyMongoRepository.findAll().stream()
            .map(PlayerPositionTendencyDocument::toDomain).toList();

        return PlayerPositionTendency.DEFAULT_TENDENCIES.stream()
            .map(defaultTendency -> storedPlayerPositionTendencies.stream()
                .filter(tendency -> tendency.getPosition().equals(defaultTendency.getPosition()))
                .findFirst()
                .orElse(defaultTendency))
            .toList();
    }
}
