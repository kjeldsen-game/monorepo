package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.player.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.player.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

        playerReadRepository.findByTeamId(teamId)
            .forEach(player -> {
                // TODO in the future we can add here an event to save the change on the salary of the player. Like previous salary information for
                //  a graph in the future
                player.negotiateSalary();
                playerWriteRepository.save(player);
            });
    }
}
