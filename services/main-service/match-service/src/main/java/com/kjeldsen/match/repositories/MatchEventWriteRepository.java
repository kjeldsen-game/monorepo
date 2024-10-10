package com.kjeldsen.match.repositories;

import com.kjeldsen.player.domain.events.MatchEvent;

public interface MatchEventWriteRepository {
    MatchEvent save(MatchEvent matchEvent);
}