package com.kjeldsen.player.persistence.adapters.mongo.training.player;

import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.repositories.training.PlayerTrainingScheduledEventReadRepository;
import com.kjeldsen.player.persistence.mongo.repositories.training.player.PlayerTrainingScheduledEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlayerTrainingScheduledEventReadRepositoryMongoAdapter implements PlayerTrainingScheduledEventReadRepository {

    private final PlayerTrainingScheduledEventMongoRepository playerTrainingScheduledEventMongoRepository;

    @Override
    public List<PlayerTrainingScheduledEvent> findAllActiveScheduledTrainings(LocalDate date) {
        return playerTrainingScheduledEventMongoRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(date);
    }

    @Override
    public List<PlayerTrainingScheduledEvent> findAllActiveScheduledTrainings() {
        return playerTrainingScheduledEventMongoRepository.findAllWhereStatusIsActive();
    }
}
