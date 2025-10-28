package com.kjeldsen.match.application.usecases.league.match;

import com.kjeldsen.lib.publishers.GenericEventPublisher;
import com.kjeldsen.match.domain.builders.LeagueStartEventBuilder;
import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.entities.ScheduledMatch;
import com.kjeldsen.match.domain.repositories.LeagueReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerateAllLeaguesMatchesUseCase {

    private final LeagueReadRepository leagueReadRepository;
    private final GenerateScheduledMatchesUseCase generateScheduledMatchesUseCase;
    private final ScheduleLeagueMatchesUseCase scheduleLeagueMatchesUseCase;
    private final GenericEventPublisher genericEventPublisher;

    public void generate() {
        List<League> leagues = leagueReadRepository.findAllByStatus(League.LeagueStatus.PRESEASON);
        leagues.forEach(league -> {
            List<ScheduledMatch> matches = generateScheduledMatchesUseCase.generate(league.getId().value(), false);
            scheduleLeagueMatchesUseCase.schedule(matches, league.getId().value());
        });
        // Get All teamIds to announce league schedule, except bot
        List<String> teamIds = leagues.stream().flatMap(league -> league.getTeams().entrySet().stream()
            .filter(entry -> !entry.getValue().isBot())).map(Map.Entry::getKey).toList();

        genericEventPublisher.publishEvent(LeagueStartEventBuilder.build(teamIds));
    }
}
