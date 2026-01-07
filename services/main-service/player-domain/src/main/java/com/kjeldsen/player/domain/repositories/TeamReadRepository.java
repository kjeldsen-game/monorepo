package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Team.TeamId;
import com.kjeldsen.player.domain.repositories.queries.FindTeamsQuery;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface TeamReadRepository {

    Optional<Team> findByTeamName(String teamName);

    Optional<Team> findByUserId(String id);

    Optional<Team> findById(Team.TeamId id);

    Page<Team> find(FindTeamsQuery query);

    List<Team> findAll();

    List<TeamId> findAllTeamIds();

    List<Team> findAllByTeamIds(List<Team.TeamId> teamIds);
}
