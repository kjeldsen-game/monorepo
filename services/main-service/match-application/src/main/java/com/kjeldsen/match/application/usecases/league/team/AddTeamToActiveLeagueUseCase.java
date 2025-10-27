package com.kjeldsen.match.application.usecases.league.team;

import com.kjeldsen.match.application.usecases.league.match.GenerateScheduledMatchesUseCase;
import com.kjeldsen.match.application.usecases.league.match.RescheduleLeagueUseCase;
import com.kjeldsen.match.application.usecases.league.match.ScheduleLeagueMatchesUseCase;
import com.kjeldsen.match.domain.factories.LeagueFactory;
import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.entities.ScheduledMatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
@RequiredArgsConstructor
public class AddTeamToActiveLeagueUseCase extends AbstractAddTeamToLeagueUseCase{

    private final RescheduleLeagueUseCase rescheduleLeagueUseCase;
    private final GenerateScheduledMatchesUseCase generateScheduledMatchesUseCase;
    private final ScheduleLeagueMatchesUseCase scheduleLeagueMatchesUseCase;

    public String addWithSpot(List<League> leaguesWithSpot, String teamName, String teamId) {
        log.info("AddTeamToActiveLeagueUseCase addWithSpot for teamName = {} teamId = {}", teamName, teamId);
        League league = leaguesWithSpot.get(ThreadLocalRandom.current().nextInt(leaguesWithSpot.size()));
        List<Map.Entry<String, League.LeagueStats>> botTeams = league.getTeams().entrySet().stream()
            .filter(entry -> entry.getValue().isBot())
            .toList();
        Map.Entry<String, League.LeagueStats> randomBotEntry =
            botTeams.get(ThreadLocalRandom.current().nextInt(botTeams.size()));

        rescheduleLeagueUseCase.rescheduleLeague(league.getId().value(), randomBotEntry.getKey(), teamId, teamName );
        league.getTeams().remove(randomBotEntry.getKey());
        LeagueFactory.createLeagueStats(league, teamName, teamId);
        leagueWriteRepository.save(league);
        return league.getId().value();
    }

    public String addWithoutSpot(String teamName, String teamId) {
        log.info("AddTeamToActiveLeagueUseCase addWithoutSpot for teamName = {} teamId = {}", teamName, teamId);

        League league = LeagueFactory.createLeague(League.LeagueStatus.ACTIVE);
        LeagueFactory.createLeagueStats(league, teamName, teamId);
        leagueWriteRepository.save(league);

        List<ScheduledMatch> scheduledMatches = generateScheduledMatchesUseCase.generate(league.getId().value(), true);
        log.info("Scheduled matches count = {}", scheduledMatches.size());
        scheduleLeagueMatchesUseCase.schedule(scheduledMatches, league.getId().value());
        return league.getId().value();
    }
}
