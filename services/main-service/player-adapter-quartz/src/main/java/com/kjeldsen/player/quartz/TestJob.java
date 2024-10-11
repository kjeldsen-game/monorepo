package com.kjeldsen.player.quartz;

import com.kjeldsen.player.application.usecases.FindAndProcessScheduledTrainingUseCase;
import com.kjeldsen.player.application.usecases.player.ProcessDeclineTrainingUseCase;
import com.kjeldsen.player.application.usecases.player.ProcessPlayerTrainingUseCase;
import com.kjeldsen.player.application.usecases.player.ProcessPotentialRiseUseCase;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestJob implements Job {

    private final ProcessPlayerTrainingUseCase processPlayerTrainingUseCase;
    private final ProcessPotentialRiseUseCase processPotentialRiseUseCase;
    private final ProcessDeclineTrainingUseCase processDeclineTrainingUseCase;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("TestJob in Kjeldsen is here!");
        // PlayerTraining, PlayerDecline, PlayerRiseUp
        processPlayerTrainingUseCase.process();
//        processPotentialRiseUseCase.process();
//        processDeclineTrainingUseCase.process();
    }
}