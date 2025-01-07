package com.kjeldsen.match.quartz;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.match.Game;
import com.kjeldsen.match.application.usecases.ExecuteMatchUseCase;
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

    private final ExecuteMatchUseCase executeMatchUseCase;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        executeMatchUseCase.execute(jobExecutionContext.getJobDetail().getJobDataMap().getString("matchId"));
    }
}
