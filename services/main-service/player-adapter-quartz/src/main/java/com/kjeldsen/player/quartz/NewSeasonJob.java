package com.kjeldsen.player.quartz;

import com.kjeldsen.player.application.usecases.*;
import com.kjeldsen.player.application.usecases.economy.*;
import com.kjeldsen.player.application.usecases.fanbase.FansManagementUsecase;
import com.kjeldsen.player.application.usecases.fanbase.UpdateLoyaltyUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewSeasonJob implements Job {

    private final ResetBillboardSponsorIncomeUseCase resetBillboardSponsorIncomeUseCase;
    private final FansManagementUsecase fansManagementUsecase;
    private final UpdateLoyaltyUseCase updateLoyaltyUseCase;
    private final UpdateSalariesTeamUseCase updateSalariesTeamUseCase;
    private final TeamReadRepository teamReadRepository;
    private final BillboardIncomeUseCase billboardIncomeUseCase;
    private final ResetBillboardIncomeUseCase resetBillboardIncomeUseCase;
    private final TeamWriteRepository teamWriteRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("New Season in Kjeldsen is here!!!!");
        // TODO
        //  NewSchedule(league)
        // DONE
        //  LoyaltyUseCase, FansUseCase, BillboardsSelection + reset, SponsorIncomeAnnualy, PlayerSalaryUpdate

//        List<Team> teams = teamReadRepository.findAll();
//        teams.forEach(team -> {
//            // Create a new Season and update the Leagues for every Team
//
//            // Process the Billboard income -> first reset when the deal ended, then pay if there is an active deal
//            billboardIncomeUseCase.pay(team.getId());
//
//            // Update the loyalty on the new Season
//            updateLoyaltyUseCase.updateLoyaltySeason(team.getId());
//
//            // Update players salaries in teams
//            updateSalariesTeamUseCase.update(team.getId());
//
//        });
//        resetBillboardIncomeUseCase.reset();
//        resetBillboardSponsorIncomeUseCase.reset(Team.Economy.IncomePeriodicity.ANNUAL);
    }
}
