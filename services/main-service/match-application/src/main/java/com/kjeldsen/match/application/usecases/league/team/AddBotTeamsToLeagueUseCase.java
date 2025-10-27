package com.kjeldsen.match.application.usecases.league.team;

import com.kjeldsen.match.application.usecases.league.GetLeagueUseCase;
import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.repositories.LeagueReadRepository;
import com.kjeldsen.match.domain.repositories.LeagueWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class AddBotTeamsToLeagueUseCase {

    private final GetLeagueUseCase getLeagueUseCase;
    private final LeagueWriteRepository leagueWriteRepository;

    public void add(Map<String, String> teams, String leagueId) {
        log.info("AddTeamToLeagueUseCase addBotsToLeague for teams = {} league = {}", teams, leagueId);
        League league = getLeagueUseCase.get(leagueId);

        for (String teamId : teams.keySet()) {
            League.LeagueStats stats = new League.LeagueStats();
            stats.setBot(true);
            stats.setName(teams.get(teamId));
            stats.setPosition(league.getTeams().size() + 1);
            league.addTeam(teamId, stats);
        }
        leagueWriteRepository.save(league);
    }
}
