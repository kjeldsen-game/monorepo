package com.kjeldsen.match.quartz;

import com.kjeldsen.match.application.usecases.ExecuteMatchUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

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
