package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.repositories.PlayerPositionTendencyWriteRepository;
import com.kjeldsen.player.persistence.mongo.documents.PlayerPositionTendencyDocument;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerPositionTendencyMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerPositionTendencyWriteRepositoryMongoAdapter implements PlayerPositionTendencyWriteRepository {

    private final PlayerPositionTendencyMongoRepository playerPositionTendencyMongoRepository;

    @Override
    public PlayerPositionTendency save(PlayerPositionTendency playerPositionTendency) {
        PlayerPositionTendencyDocument newOrFoundDocument = playerPositionTendencyMongoRepository.findByPosition(playerPositionTendency.getPosition())
            .orElseGet(() -> PlayerPositionTendencyDocument.from(playerPositionTendency));
        newOrFoundDocument.setTendencies(playerPositionTendency.getTendencies());

        return playerPositionTendencyMongoRepository.save(newOrFoundDocument).toDomain();
    }
}
