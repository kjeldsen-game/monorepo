package com.kjeldsen.player.domain.repositories.player;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.FindPlayersQuery;

import java.util.List;
import java.util.Optional;

public interface PlayerReadRepository {
    Optional<Player> findOneById(Player.PlayerId id);

    List<Player> find(FindPlayersQuery query);

    List<Player> findByTeamId(Team.TeamId teamId);

}
