package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlayerTrainingScheduledEventReadRepository {

    List<PlayerTrainingScheduledEvent> findAllActiveScheduledTrainings(LocalDate date);

    List<PlayerTrainingScheduledEvent> findAllActiveScheduledTrainings();
}
