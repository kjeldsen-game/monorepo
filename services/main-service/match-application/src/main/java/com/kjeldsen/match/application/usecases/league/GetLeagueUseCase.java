package com.kjeldsen.match.application.usecases.league;

import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.repositories.LeagueReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetLeagueUseCase {

    private final LeagueReadRepository leagueReadRepository;

    public League get(final String leagueId) {
        return leagueReadRepository.findById(League.LeagueId.of(leagueId)).orElseThrow(
            () -> new RuntimeException("League not found"));
    }
}
