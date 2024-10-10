package com.kjeldsen.player.rest.quartzTestTODORemove;

import com.kjeldsen.player.application.usecases.ExampleQuartzTestUseCase;
import com.kjeldsen.player.application.usecases.ResetBillboardSponsorIncomeUseCase;
import com.kjeldsen.player.domain.Team;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewWeekJob implements Job {

    @Autowired
    private ResetBillboardSponsorIncomeUseCase resetBillboardSponsorIncomeUseCase;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Running NewWeekJob");

        resetBillboardSponsorIncomeUseCase.reset(Team.Economy.IncomePeriodicity.WEEKLY);
    }
}
