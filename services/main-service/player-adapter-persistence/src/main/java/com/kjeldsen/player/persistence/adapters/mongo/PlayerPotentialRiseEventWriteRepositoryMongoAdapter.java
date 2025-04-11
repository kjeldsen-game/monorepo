package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import com.kjeldsen.player.domain.repositories.PlayerPotentialRiseEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerPotentialRiseEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerPotentialRiseEventWriteRepositoryMongoAdapter implements PlayerPotentialRiseEventWriteRepository {

    private final PlayerPotentialRiseEventMongoRepository playerPotentialRiseEventMongoRepository;

    @Override
    public PlayerPotentialRiseEvent save(PlayerPotentialRiseEvent playerPotentialRiseEvent) {
        return playerPotentialRiseEventMongoRepository.save(playerPotentialRiseEvent);
    }
}
