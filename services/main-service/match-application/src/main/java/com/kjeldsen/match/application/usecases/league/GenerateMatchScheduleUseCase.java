package com.kjeldsen.match.application.usecases.league;

import com.kjeldsen.match.domain.entities.League;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerateMatchScheduleUseCase {

        private final GetLeagueUseCase getLeagueUseCase;

        public List<ScheduledMatch> generate(String leagueId) {
        log.info("GenerateMatchScheduleUseCase for league={}", leagueId);

        League league = getLeagueUseCase.get(leagueId);
        List<String> teamIds = league.getTeams().keySet().stream().toList();

        List<ScheduledMatch> scheduledMatches = new ArrayList<>();

            LocalDateTime startDate = LocalDateTime.now();
            int numRounds = teamIds.size() - 1;
            int matchIntervalMinutes = 1;

            for (int round = 0; round < numRounds * 2; round++) {
                boolean isSecondRound = round >= numRounds;

//                startDate = startDate.plusMinutes(matchIntervalMinutes);
                startDate = startDate.plusDays(matchIntervalMinutes);
                generateMatchesForRound(teamIds, scheduledMatches, startDate, isSecondRound);

                try {
                    Collections.rotate(teamIds.subList(1, teamIds.size()), 1);
                } catch (UnsupportedOperationException e) {
                    List<String> mutableTeams = new ArrayList<>(teamIds);
                    Collections.rotate(mutableTeams.subList(1, mutableTeams.size()), 1);
                    teamIds = mutableTeams;
                }
            }
        return scheduledMatches;
    }

    private void generateMatchesForRound(List<String> teamIds, List<ScheduledMatch> scheduledMatches,
                                         LocalDateTime matchDay, boolean swapHomeAway) {
        int numMatchesPerRound = teamIds.size() / 2;
        for (int match = 0; match < numMatchesPerRound; match++) {
            String home = teamIds.get(match);
            String away = teamIds.get(teamIds.size() - 1 - match);

            if (swapHomeAway) {
                String temp = home;
                home = away;
                away = temp;
            }

            scheduledMatches.add(new ScheduledMatch(home, away, matchDay.plusMinutes(3)));
        }
    }

    public record ScheduledMatch(String homeTeamId, String awayTeamId, LocalDateTime date) { }
}
