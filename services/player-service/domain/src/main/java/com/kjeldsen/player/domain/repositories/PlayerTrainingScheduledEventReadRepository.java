package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;

import java.time.LocalDate;
import java.util.List;

public interface PlayerTrainingScheduledEventReadRepository {

    List<PlayerTrainingScheduledEvent> findAllActiveScheduledTrainings(LocalDate date);

}
