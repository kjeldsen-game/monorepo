package com.kjeldsen.player.application.usecases.economy;


import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResetBillboardSponsorIncomeUseCase {

    private final TeamReadRepository teamReadRepository;
    private final TeamWriteRepository teamWriteRepository;

    public void reset(Team.Economy.IncomePeriodicity periodicity) {
        log.info("ResetBillboardSponsorIncomeUseCase for periodicity {}", periodicity);
        teamReadRepository.findAll().forEach(team -> {
            log.info("Team {} has been reset", team.getName());
            team.getEconomy().resetSponsorIncome(periodicity);
            teamWriteRepository.save(team);
        });
    }
}
