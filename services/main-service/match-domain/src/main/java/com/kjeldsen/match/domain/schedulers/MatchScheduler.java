package com.kjeldsen.match.domain.schedulers;

import java.time.Instant;

public interface MatchScheduler {
    void scheduleMatch(String matchId, Instant matchDate);
}
