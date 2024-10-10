package com.kjeldsen.player.rest.quartzTestTODORemove;

import com.kjeldsen.player.application.usecases.ExampleQuartzTestUseCase;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EveryMinuteJob implements Job {

    @Autowired
    private ExampleQuartzTestUseCase exampleQuartzTestUseCase;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Running EveryMinuteJob");
        exampleQuartzTestUseCase.execute();
    }
}
