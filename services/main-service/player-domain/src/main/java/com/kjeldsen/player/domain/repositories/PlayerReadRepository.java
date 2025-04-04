package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.queries.FilterMarketPlayersQuery;
import com.kjeldsen.player.domain.repositories.queries.FindPlayersQuery;

import java.util.List;
import java.util.Optional;

public interface PlayerReadRepository {

    List<Player> findAll();

    Optional<Player> findOneById(Player.PlayerId id);

    List<Player> find(FindPlayersQuery query);

    List<Player> findByTeamId(Team.TeamId teamId);

    List<Player> findByPlayersIds(List<String> playersIds);

    List<Player> filterMarketPlayers(FilterMarketPlayersQuery inputQuery);

    List<Player> findPlayerUnderAge(Integer age);

    List<Player> findPlayerOverAge(Integer age);
}
