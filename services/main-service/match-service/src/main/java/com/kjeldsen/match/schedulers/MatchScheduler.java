package com.kjeldsen.match.schedulers;

import java.time.Instant;

public interface MatchScheduler {
    void scheduleMatch(String matchId, Instant matchDate);
}
