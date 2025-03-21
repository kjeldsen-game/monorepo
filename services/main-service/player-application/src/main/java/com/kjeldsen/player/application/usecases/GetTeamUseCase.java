package com.kjeldsen.player.application.usecases;

import com.kjeldsen.market.domain.exceptions.TeamNotFoundException;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GetTeamUseCase {

    private final TeamReadRepository teamReadRepository;

    public Team get(String userId) {
        return teamReadRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException(String.format("Team not found for user with ID %s", userId)));
    }

    public Team get(Team.TeamId teamId) {
        return teamReadRepository.findById(teamId).orElseThrow(
            TeamNotFoundException::new);
    }
}
