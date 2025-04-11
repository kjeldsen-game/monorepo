package com.kjeldsen.player.application.usecases.player;

import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.TeamModifiers;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateTeamModifiersUseCase {

    private final TeamWriteRepository teamWriteRepository;
    private final GetTeamUseCase getTeamUseCase;

    public void update(String teamId, TeamModifiers modifiers) {

        Team team =  getTeamUseCase.get(Team.TeamId.of(teamId));

        team.setTeamModifiers(modifiers);
        teamWriteRepository.save(team);
    }
}
