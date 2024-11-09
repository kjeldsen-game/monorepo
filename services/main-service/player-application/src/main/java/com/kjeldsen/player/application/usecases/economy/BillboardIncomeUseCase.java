package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BillboardIncomeUseCase {

    private final TeamReadRepository teamReadRepository;
    private final CreateTransactionUseCase createTransactionUseCase;

    public void pay(Team.TeamId teamId) {
        log.info("BillboardIncomeUseCase for team {}", teamId);

        Team team = teamReadRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        if (team.getEconomy().getBillboardDeal() == null) {
            throw new RuntimeException("BillboardDeal not found");
        }

        createTransactionUseCase.create(teamId, team.getEconomy().getBillboardDeal().getOffer(),
            Transaction.TransactionType.BILLBOARDS);
    }
}
