package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Team.TeamId;
import java.util.List;
import java.util.Optional;

public interface TeamReadRepository {

    Optional<Team> findOneById(TeamId id);

    Optional<Team> findByUserId(String id);

    Optional<Team> findById(Team.TeamId id);

    List<Team> find(FindTeamsQuery query);

    List<Team> findAll();
}
