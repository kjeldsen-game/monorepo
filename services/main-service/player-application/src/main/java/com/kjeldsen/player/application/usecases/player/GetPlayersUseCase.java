package com.kjeldsen.player.application.usecases.player;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
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
}


