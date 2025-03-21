package com.kjeldsen.match.application.usecases.league;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.domain.EventId;
import com.kjeldsen.match.domain.clients.TeamClientMatch;
import com.kjeldsen.match.domain.clients.models.team.TeamDTO;
import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.publisher.LeagueEventPublisher;
import com.kjeldsen.match.domain.repositories.LeagueReadRepository;
import com.kjeldsen.match.domain.repositories.LeagueWriteRepository;
import com.kjeldsen.player.domain.events.LeagueEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddTeamToLeagueUseCase {

    private final LeagueWriteRepository leagueWriteRepository;
    private final LeagueReadRepository leagueReadRepository;
    private final TeamClientMatch teamClient;
    private final LeagueEventPublisher leagueEventPublisher;

    public void add(String teamId, BigDecimal teamValue) {
        Random random = new Random();
        log.info("AddTeamToLeagueUseCase for teamId={} with teamValue={}", teamId, teamValue );
        // TODO add validation to check if the team don't have already assigned league
        TeamDTO team = teamClient.getTeam(teamId, SecurityUtils.getCurrentUserToken());

        List<League> leagues = leagueReadRepository.findAll();
        // Filter leagues with same tier and it's not full yet
        List<League> filtered = leagues.stream()
            .filter(league -> league.getTeams().size() < 10)  //tier.equals(league.getTier()) &&
            .toList();

        // Get the random league or create a new one
        League league = filtered.isEmpty() ? League.builder()
            .id(League.LeagueId.generate())
            .startedAt(Instant.now())
            .teams(new HashMap<>())
            .tier(1)
            .build() : filtered.get(Math.abs(new Random().nextInt()) % leagues.size());

        League.LeagueStats stats = new League.LeagueStats();
        stats.setName(team.getName());
        stats.setPosition(league.getTeams().size() + 1); // Assign last position
        league.addTeam(teamId, stats);
        // The league is full, publish event and schedule matches
//        if (league.getTeams().size() == 4) {
//            league.setScheduledMatches(true);
//            scheduleLeagueEventPublisher.publish(ScheduleLeagueEvent.builder()
//                .teamsIds(league.getTeams().keySet().stream().toList())
//                .leagueId(league.getId().value())
//                .id(EventId.generate())
//                .occurredAt(Instant.now())
//                .build());
//        }
        // Save leagueId for the team as well to be able to filter league data
        League leagueSaved = leagueWriteRepository.save(league);
        //team.setLeagueId(leagueSaved.getId().value());
        leagueEventPublisher.publishLeagueEvent(LeagueEvent.builder().id(EventId.generate())
            .teamId(team.getId()).leagueId(league.getId().value()).occurredAt(Instant.now()).build());
    }
}
