package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;

import java.util.List;
import java.util.Optional;

public interface PlayerReadRepository {
    Optional<Player> findOneById(Player.PlayerId id);

    List<Player> find(FindPlayersQuery query);

    List<Player> findByTeamId(Team.TeamId teamId);

    List<Player> findByPlayersIds(List<String> playersIds);
}
