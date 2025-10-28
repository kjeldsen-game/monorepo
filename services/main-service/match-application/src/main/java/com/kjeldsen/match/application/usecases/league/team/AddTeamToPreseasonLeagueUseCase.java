package com.kjeldsen.match.application.usecases.league.team;

import com.kjeldsen.match.domain.factories.LeagueFactory;
import com.kjeldsen.match.domain.entities.League;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddTeamToPreseasonLeagueUseCase extends AbstractAddTeamToLeagueUseCase {

    public String add(String teamName, String teamId) {
        log.info("AddTeamToPreseasonLeagueUseCase for teamName = {} teamId = {}", teamName, teamId);
        List<League> leaguesWithSpace = leagueReadRepository.findAllByStatus(League.LeagueStatus.PRESEASON)
            .stream().filter(league -> league.getTeams().size() < 10).toList();

        League league = leaguesWithSpace.isEmpty() ?
            LeagueFactory.createLeague(League.LeagueStatus.PRESEASON) :
            leaguesWithSpace.get(ThreadLocalRandom.current().nextInt(leaguesWithSpace.size()));

        LeagueFactory.createLeagueStats(league, teamName, teamId);
        leagueWriteRepository.save(league);
        return league.getId().value();
    }
}
