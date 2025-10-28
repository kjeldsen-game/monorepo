package com.kjeldsen.match.application.usecases.league.match;

import com.kjeldsen.lib.events.EventId;
import com.kjeldsen.lib.events.LeagueStartBotTeamsCreationEvent;
import com.kjeldsen.lib.publishers.GenericEventPublisher;
import com.kjeldsen.match.application.usecases.league.GetLeagueUseCase;
import com.kjeldsen.match.domain.entities.League;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TriggerBotsCreationUseCase {

    public static final int REQUIRED_TEAM_COUNT = 10;
    private final GetLeagueUseCase getLeagueUseCase;
    private final GenericEventPublisher eventPublisher;

    public List<String> trigger(String leagueId) {
        log.info("TriggerBotsCreationUseCase for league={}", leagueId);
        League league = getLeagueUseCase.get(leagueId);
        List<String> teamIds = league.getTeams().keySet().stream().toList();

        // Check the count of the teams in the league, if its fewer bots have to generated and assigned to the team
        if (teamIds.size() < REQUIRED_TEAM_COUNT) {
            int missingTeams = Math.abs(teamIds.size() - REQUIRED_TEAM_COUNT);
            eventPublisher.publishEvent(LeagueStartBotTeamsCreationEvent.builder()
                .id(EventId.generate())
                .occurredAt(Instant.now())
                .count(missingTeams)
                .leagueId(leagueId)
                .build());

            league = getLeagueUseCase.get(leagueId);
            teamIds = league.getTeams().keySet().stream().toList();
        }
        return teamIds;
    }
}
