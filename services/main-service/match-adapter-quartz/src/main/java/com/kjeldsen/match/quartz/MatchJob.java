package com.kjeldsen.match.quartz;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.match.Game;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.MatchReport;
import com.kjeldsen.match.publisher.MatchEventPublisher;
import com.kjeldsen.match.repositories.MatchEventWriteRepository;
import com.kjeldsen.match.repositories.MatchReadRepository;
import com.kjeldsen.match.repositories.MatchWriteRepository;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.MatchEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchJob implements Job {

    private final MatchWriteRepository matchWriteRepository;
    private final MatchReadRepository matchReadRepository;
    private final TeamReadRepository teamReadRepository;
    private final MatchEventPublisher matchEventPublisher;
    private final MatchEventWriteRepository matchEventWriteRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Optional<Match> match = Optional.ofNullable(matchReadRepository.findOneById(
            jobExecutionContext.getJobDetail().getJobDataMap().getString("matchId")))
            .orElseThrow(() -> new RuntimeException("Match not found"));

        match.ifPresent(m -> {
            if (m.getStatus() == Match.Status.ACCEPTED) {
                GameState state = Game.play(m);
                Map<String, Integer> attendance = getMatchAttendance(m);
                MatchReport report = new MatchReport(state, state.getPlays(), m.getHome(),
                    m.getAway(), attendance.get("homeAttendance"), attendance.get("awayAttendance"));

                m.setMatchReport(report);

                MatchEvent matchEvent = MatchEvent.builder()
                    .id(EventId.generate())
                    .occurredAt(InstantProvider.now())
                    .matchId(m.getId())
                    .homeTeamId(m.getHome().getId())
                    .awayTeamId(m.getAway().getId())
                    .homeScore(m.getMatchReport().getHomeScore())
                    .awayScore(m.getMatchReport().getAwayScore())
                    .homeAttendance(m.getMatchReport().getHomeAttendance())
                    .awayAttendance(m.getMatchReport().getAwayAttendance())
                    .build();

                matchEventWriteRepository.save(matchEvent);
                matchEventPublisher.publishMatchEvent(matchEvent);

            }
            matchWriteRepository.save(m);
        });
    }

    private Map<String, Integer> getMatchAttendance(Match match) {
        com.kjeldsen.player.domain.Team homeTeam = teamReadRepository.findById(Team.TeamId.of(match.getHome().getId()))
            .orElseThrow(() -> new RuntimeException("Team not found"));
        Integer capacity = homeTeam.getBuildings().getStadium().getSeats();
        int homeAttendance = Math.round(homeTeam.getFans().getTotalFans() * 0.8f);

        com.kjeldsen.player.domain.Team awayTeam = teamReadRepository.findById(Team.TeamId.of(match.getAway().getId()))
            .orElseThrow(() -> new RuntimeException("Team not found"));
        Integer awayAttendance = awayTeam.getFans().getTotalFans();

        if (homeAttendance + awayAttendance > capacity) {
            float scaleFactor = (float) (homeAttendance + awayAttendance) / capacity;
            homeAttendance = Math.round(homeAttendance * scaleFactor);
            awayAttendance = Math.round(awayAttendance * scaleFactor);
        }
        return Map.of(
            "homeAttendance", homeAttendance,
            "awayAttendance", awayAttendance
        );
    }
}
