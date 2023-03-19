package com.kjeldsen.player.domain.repositories;


import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.TeamId;

public interface TeamWriteRepository {
    TeamId save(Team team, String userId);
}
