package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.Team;

public interface TeamWriteRepository {
    Team.TeamId save(Team team, String userId);
}
