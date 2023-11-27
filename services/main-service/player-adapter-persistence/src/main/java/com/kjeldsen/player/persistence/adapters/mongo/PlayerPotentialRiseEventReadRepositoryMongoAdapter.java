package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import com.kjeldsen.player.domain.repositories.PlayerPotentialRiseEventReadRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerPotentialRiseEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@RequiredArgsConstructor
public class PlayerPotentialRiseEventReadRepositoryMongoAdapter implements PlayerPotentialRiseEventReadRepository {
    private final PlayerPotentialRiseEventMongoRepository playerPotentialRiseEventMongoRepository;

    @Override
    public List<PlayerPotentialRiseEvent> findAllByPlayerId(Player.PlayerId playerId) {
        return playerPotentialRiseEventMongoRepository.findAllByPlayerId(playerId);
    }
}
