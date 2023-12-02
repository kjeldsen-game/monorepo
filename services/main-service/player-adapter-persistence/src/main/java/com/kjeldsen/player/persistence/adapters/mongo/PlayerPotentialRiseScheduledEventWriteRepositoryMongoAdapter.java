package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.events.PlayerPotentialRiseScheduledEvent;
import com.kjeldsen.player.domain.repositories.PlayerPotentialRiseScheduledEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerPotentialRiseScheduledEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerPotentialRiseScheduledEventWriteRepositoryMongoAdapter implements PlayerPotentialRiseScheduledEventWriteRepository {
    private final PlayerPotentialRiseScheduledEventMongoRepository playerPotentialRiseScheduledEventMongoRepository;

    @Override
    public PlayerPotentialRiseScheduledEvent save(PlayerPotentialRiseScheduledEvent playerPotentialRiseScheduledEvent) {
        return playerPotentialRiseScheduledEventMongoRepository.save(playerPotentialRiseScheduledEvent);
    }
}
