package com.kjeldsen.match.application.usecases.league.match;

import com.kjeldsen.match.domain.entities.ScheduledMatch;
import com.kjeldsen.match.domain.schedulers.JobQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerateScheduledMatchesUseCase {

    public static final int INTERVALS_BETWEEN_ROUNDS = 1;
    public static final int MAX_HALF_ROUNDS = TriggerBotsCreationUseCase.REQUIRED_TEAM_COUNT - 1;
    private final TriggerBotsCreationUseCase triggerBotsCreationUseCase;
    private final JobQueryService jobQueryService;

    public List<ScheduledMatch> generate(String leagueId, boolean isLeagueActive) {
        log.info("GenerateMatchScheduleUseCase for league={}", leagueId);

        List<String> teamIds = new ArrayList<>(triggerBotsCreationUseCase.trigger(leagueId));
        List<List<ScheduledMatch>> scheduledMatches = new ArrayList<>();

        int daysToBePlayed = calculateDays(isLeagueActive);
        int rounds = Math.min(daysToBePlayed, MAX_HALF_ROUNDS);
        int reverseRounds = isLeagueActive ? Math.max(0, daysToBePlayed - MAX_HALF_ROUNDS) : MAX_HALF_ROUNDS;

        for (int round = 0; round < rounds; round++) {
            boolean swap = round % 2 == 1;
            List<ScheduledMatch> matches = generateMatchesForRound(teamIds, swap);
            Collections.rotate(teamIds.subList(1, teamIds.size()), 1);
            scheduledMatches.add(matches);
        }

        Collections.shuffle(scheduledMatches);

        for (int round = 0; round < reverseRounds; round++ ) {
            List<ScheduledMatch> originalRound = scheduledMatches.get(round);
            List<ScheduledMatch> reversedRound = new ArrayList<>();
            for(ScheduledMatch match : originalRound) {
                reversedRound.add(ScheduledMatch.builder().homeTeamId(match.getAwayTeamId())
                    .awayTeamId(match.getHomeTeamId()).build());
            }
            scheduledMatches.add(reversedRound);
        }

        LocalDateTime startDate = LocalDateTime.now();
        for (List<ScheduledMatch> round: scheduledMatches) {
            startDate = startDate.plusDays(INTERVALS_BETWEEN_ROUNDS);
            for (ScheduledMatch match : round) {
                match.setDate(startDate);
            }
        }

        return scheduledMatches.stream().flatMap(List::stream).collect(Collectors.toList());
    }

    private static List<ScheduledMatch> generateMatchesForRound(
        List<String> teams,
        boolean swapHomeAway
    ) {
        List<ScheduledMatch> matches = new ArrayList<>();
        int numMatches = teams.size() / 2;

        for (int i = 0; i < numMatches; i++) {
            String home = teams.get(i);
            String away = teams.get(teams.size() - 1 - i);
            if (swapHomeAway) {
                String tmp = home;
                home = away;
                away = tmp;
            }
            matches.add(ScheduledMatch.builder().homeTeamId(home).awayTeamId(away).build());
        }
        return matches;
    }


    private int calculateDays(boolean isLeagueActive) {
        if (isLeagueActive) {
            Optional<ZonedDateTime> leagueEndDate = jobQueryService.getNextFireTime(
                "leagueEndJob", "league");
            if (leagueEndDate.isPresent()) {
                return (int) ChronoUnit.DAYS.between(ZonedDateTime.now().toInstant(),
                    leagueEndDate.get().toInstant()) - 1;
            } else {
                throw new RuntimeException("League end date was not found");
            }
        } else {
            return MAX_HALF_ROUNDS * 2;
        }
    }
}
