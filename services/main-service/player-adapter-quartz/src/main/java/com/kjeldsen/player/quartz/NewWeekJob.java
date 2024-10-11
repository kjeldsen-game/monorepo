package com.kjeldsen.player.quartz;

import com.kjeldsen.player.application.usecases.*;
import com.kjeldsen.player.application.usecases.economy.BuildingMaintenanceExpenseUseCase;
import com.kjeldsen.player.application.usecases.economy.ResetBillboardSponsorIncomeUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NewWeekJob implements Job {

    // Run multiple useCases that should be executed every week
    private final BuildingMaintenanceExpenseUseCase buildingMaintenanceExpenseUseCase;
    private final PaySalariesTeamUseCase paySalariesTeamUseCase;
    private final ResetBillboardSponsorIncomeUseCase resetBillboardSponsorIncomeUseCase;
    // Repositories
    private final TeamReadRepository teamReadRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("New week in Kjeldsen is here!!");
        // SponsorIncomeWeekly, SalaryPayout, BuildingMaintenance
//        resetBillboardSponsorIncomeUseCase.reset(Team.Economy.IncomePeriodicity.WEEKLY);
//        List<Team> teams = teamReadRepository.findAll();
//        teams.forEach(team -> {
//            paySalariesTeamUseCase.pay(team.getId());
//            buildingMaintenanceExpenseUseCase.expense(team.getId());
//        });
    }
}
