package com.kjeldsen.league.rest.mapper;

import com.kjeldsen.league.domain.League;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import org.mapstruct.Mapper;

@Mapper
public interface IdMapper {

    default String map(Player.PlayerId playerId) {
        return playerId.value();
    }

    default String map(Team.TeamId teamId) {
        return teamId.value();
    }

    default String map(League.LeagueId leagueId) {return leagueId.value(); }
}