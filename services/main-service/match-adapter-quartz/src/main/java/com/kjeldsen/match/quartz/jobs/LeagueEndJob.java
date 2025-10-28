package com.kjeldsen.match.quartz.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LeagueEndJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("League End Job");
    }
}
