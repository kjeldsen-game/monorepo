package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;

import java.util.List;
import java.util.Optional;

public interface PlayerTrainingEventReadRepository {
    List<PlayerTrainingEvent> findAllByPlayerId(Player.PlayerId playerId);

    Optional<PlayerTrainingEvent> findLastByPlayerTrainingEvent(String playerTrainingEventId);

    // TeamId to prevent fetching successful trainings from older teams
    List<PlayerTrainingEvent> findAllSuccessfulByPlayerIdAndTeamId(Player.PlayerId playerId, Team.TeamId teamId);

}
