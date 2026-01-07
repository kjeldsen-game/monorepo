package com.kjeldsen.player.application.usecases.player;

import com.kjeldsen.lib.events.NotificationEvent;
import com.kjeldsen.lib.publishers.GenericEventPublisher;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerStatus;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.exceptions.PlayerNotFoundException;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.queries.FindPlayersQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class GetPlayersUseCase {

    private final PlayerReadRepository playerReadRepository;

    public List<Player> get(String teamId) {
        return playerReadRepository.findByTeamId(Team.TeamId.of(teamId));
    }

    public Player get(Player.PlayerId playerId) {
        return playerReadRepository.findOneById(playerId)
            .orElseThrow(PlayerNotFoundException::new);
    }

    public List<Player> get(PlayerStatus status, String teamId, PlayerPosition position, Integer size, Integer page) {
        FindPlayersQuery query = FindPlayersQuery.builder()
            .position(position)
            .teamId(Team.TeamId.of(teamId))
            .status(status)
            .size(size)
            .page(page)
            .build();
        return playerReadRepository.find(query);
    }
}


