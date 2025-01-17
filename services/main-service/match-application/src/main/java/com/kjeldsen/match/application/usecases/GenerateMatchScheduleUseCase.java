package com.kjeldsen.match.application.usecases;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerateMatchScheduleUseCase {

        public List<ScheduledMatch> generate(List<String> teamIds) {
        log.info("GenerateMatchScheduleUseCase for the {} teams", teamIds.size());
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

            for (ScheduledMatch match : scheduledMatches) {
                log.info("HomeId={} AwayId={} date={}", match.homeTeamId(), match.awayTeamId(), match.date().toString());
            }

        return scheduledMatches;
    }

//    public List<ScheduledMatch> generate(List<String> teamIds) {
//        log.info("GenerateMatchScheduleUseCase for the {} teams", teamIds.size());
//        List<ScheduledMatch> scheduledMatches = new ArrayList<>();
//        LocalDate startDate = LocalDate.now();
//        int numRounds = teamIds.size() - 1;
//        int restDays = 3;
//
//        for (int round = 0; round < numRounds * 2; round++) {
//            boolean isSecondRound = round >= numRounds;
//            LocalDate matchDate = startDate.plus(round * restDays, ChronoUnit.DAYS);
//            LocalDateTime matchDay = matchDate.atTime(15, 0);  // 15:00 = 3 PM
//            generateMatchesForRound(teamIds, scheduledMatches, matchDay, isSecondRound);
//
//            try {
//                Collections.rotate(teamIds.subList(1, teamIds.size()), 1);
//            } catch (UnsupportedOperationException e) {
//                List<String> mutableTeams = new ArrayList<>(teamIds);
//                Collections.rotate(mutableTeams.subList(1, mutableTeams.size()), 1);
//                teamIds = mutableTeams;
//            }
//        }
//
//        for (ScheduledMatch scheduledMatch : scheduledMatches) {
//            log.info("Scheduled Match: HomeTeam={},  AwayTeam={},  date={}",
//                scheduledMatch.homeTeamId(), scheduledMatch.awayTeamId(), scheduledMatch.date());
//        }
//
//        return scheduledMatches;
//    }

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

            scheduledMatches.add(new ScheduledMatch(home, away, matchDay.plusMinutes(5)));
        }
    }

    public record ScheduledMatch(String homeTeamId, String awayTeamId, LocalDateTime date) { }
}
