package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
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

    private final TeamReadRepository teamReadRepository;
    private final PlayerWriteRepository playerWriteRepository;
    private final PlayerReadRepository playerReadRepository;

    public void update(Team.TeamId teamId) {
        log.info("UpdateSalariesTeamUseCase team with id {} ", teamId);

        teamReadRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        List<Player> players = playerReadRepository.findByTeamId(teamId);
        if(players.isEmpty()){
            throw new RuntimeException("No players found for team with id" + teamId);
        }

        players.forEach(player -> {
            // TODO in the future we can add here an event to save the change on the salary of the player. Like previous salary information for
            //  a graph in the future
            player.negotiateSalary();
            playerWriteRepository.save(player);
        });
    }
}
