package com.kjeldsen.player.quartz;

import com.kjeldsen.player.application.usecases.fanbase.FansManagementUsecase;
import com.kjeldsen.player.application.usecases.fanbase.UpdateLoyaltyUseCase;
import com.kjeldsen.player.domain.Team;
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
public class EndSeasonJob implements Job {

    private final UpdateLoyaltyUseCase updateLoyaltyUseCase;
    private final FansManagementUsecase fansManagementUsecase;
    private final TeamReadRepository teamReadRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Season End in Kjeldsen is here");
        // FansSeasonEndUpdate, LoyaltyReset

//        teamReadRepository.findAll().forEach(team -> {
//            fansManagementUsecase.update(team.getId(), Team.Fans.ImpactType.SEASON_END);
//            updateLoyaltyUseCase.resetLoyalty(team.getId());
//        });
    }
}
