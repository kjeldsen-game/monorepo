package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResetBillboardIncomeUseCase {

    private final TeamReadRepository teamReadRepository;
    private final TeamWriteRepository teamWriteRepository;

    public void reset() {
        log.info("ResetBillboardIncomeUseCase for every team");
        List<Team> teams = teamReadRepository.findAll();
        teams.forEach(team -> {
            Team.Economy.BillboardDeal deal = team.getEconomy().getBillboardDeal();
            System.out.println(deal);
            if (deal != null) {
                // TODO tmp
                team.getEconomy().setBillboardDeal(null);
                teamWriteRepository.save(team);
//                Integer seasonNumber = team.getActualSeason();
//                if (deal.getEndSeason() - seasonNumber == 0) { // Deal expired
//                    team.getEconomy().setBillboardDeal(null);
//                    teamWriteRepository.save(team);
//                }
            }
        });
    }
}
