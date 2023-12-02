package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.events.PlayerPotentialRiseScheduledEvent;
import com.kjeldsen.player.domain.repositories.PlayerPotentialRiseScheduledEventReadRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerPotentialRiseScheduledEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlayerPotentialRiseScheduledEventReadRepositoryMongoAdapter implements PlayerPotentialRiseScheduledEventReadRepository {
    private final PlayerPotentialRiseScheduledEventMongoRepository playerPotentialRiseScheduledEventMongoRepository;

    @Override
    public List<PlayerPotentialRiseScheduledEvent> findAllActiveScheduledPotentialRise(LocalDate date) {
        return playerPotentialRiseScheduledEventMongoRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(date);
    }
}
