package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.PlayerPotentialRiseScheduledEvent;

import java.time.LocalDate;
import java.util.List;

public interface PlayerPotentialRiseScheduledEventReadRepository {

    List<PlayerPotentialRiseScheduledEvent> findAllActiveScheduledPotentialRise(LocalDate date);

}
