package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.exceptions.TeamNotFoundException;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpdateSalariesTeamUseCase {

    private final PlayerWriteRepository playerWriteRepository;
    private final PlayerReadRepository playerReadRepository;

    public void update(Team.TeamId teamId) {
        log.info("UpdateSalariesTeamUseCase team with id {} ", teamId);

        List<Player> players = playerReadRepository.findByTeamId(teamId);
        if(players.isEmpty()){
            throw new TeamNotFoundException();
        }

        players.forEach(player -> {
            player.negotiateSalary();
            playerWriteRepository.save(player);
        });
    }
}
