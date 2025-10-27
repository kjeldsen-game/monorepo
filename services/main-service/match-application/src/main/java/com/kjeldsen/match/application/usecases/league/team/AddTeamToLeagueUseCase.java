package com.kjeldsen.match.application.usecases.league.team;

import com.kjeldsen.match.domain.entities.League;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddTeamToLeagueUseCase extends AbstractAddTeamToLeagueUseCase {

    private final AddTeamToPreseasonLeagueUseCase addTeamToPreseasonLeagueUseCase;
    private final AddTeamToActiveLeagueUseCase addTeamToActiveLeagueUseCase;

    public String add(String teamId, String teamName) {
        log.info("AddTeamToLeagueUseCase add for team = {} teamName = {}", teamId, teamName);
        List<League> activeLeagues = leagueReadRepository.findAllByStatus(League.LeagueStatus.ACTIVE);
        if (activeLeagues.isEmpty()) {
           return addTeamToPreseasonLeagueUseCase.add(teamName, teamId);
        } else {
            List<League> leaguesWithSpot = activeLeagues.stream().filter(league ->
                league.getTeams().values().stream().anyMatch(League.LeagueStats::isBot)).toList();
            if (leaguesWithSpot.isEmpty()) {
                return addTeamToActiveLeagueUseCase.addWithoutSpot(teamName, teamId);
            } else {
                return addTeamToActiveLeagueUseCase.addWithSpot(leaguesWithSpot, teamName, teamId);
            }
        }
    }
}
