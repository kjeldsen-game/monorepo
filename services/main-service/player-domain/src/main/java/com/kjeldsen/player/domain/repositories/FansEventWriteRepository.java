package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.FansEvent;

public interface FansEventWriteRepository {
    FansEvent save(FansEvent fansEvent);
}
