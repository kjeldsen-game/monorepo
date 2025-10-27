package com.kjeldsen.match.application.usecases.league.match;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.repositories.MatchReadRepository;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RescheduleLeagueUseCase {

    private final MatchReadRepository matchReadRepository;
    private final MatchWriteRepository matchWriteRepository;

    public void rescheduleLeague(String leagueId, String oldTeamId, String newTeamId, String teamName ) {
        log.info("RescheduleLeagueUseCase for league = {} oldTeamId = {} newTeamId = {}", leagueId, oldTeamId, newTeamId);
        List<Match> matches = matchReadRepository.findMatchesByLeagueIdAndTeamIdAndStatus(
            oldTeamId, leagueId, Match.Status.SCHEDULED);
        matches.forEach(match -> {
            if (match.getAway().getId().equals(oldTeamId)) {
                replaceTeam(match.getAway(), newTeamId, teamName);
            } else {
                replaceTeam(match.getHome(), newTeamId, teamName);
            }
        });
        matchWriteRepository.saveAll(matches);
    }

    private void replaceTeam(Team team, String teamId, String teamName) {
        team.setId(teamId);
        team.setSpecificLineup(false);
        team.setBench(null);
        team.setPlayers(null);
        team.setName(teamName);
    }
}
