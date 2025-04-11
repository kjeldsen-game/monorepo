package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.domain.exceptions.BillboardDealNotFoundException;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BillboardIncomeUseCase {

    private final GetTeamUseCase getTeamUseCase;
    private final CreateTransactionUseCase createTransactionUseCase;

    public void pay(Team.TeamId teamId) {
        log.info("BillboardIncomeUseCase for team {}", teamId);

        Team team = getTeamUseCase.get(teamId);

        if (team.getEconomy().getBillboardDeal() == null) {
            throw new BillboardDealNotFoundException();
        }

        createTransactionUseCase.create(teamId, team.getEconomy().getBillboardDeal().getOffer(),
            Transaction.TransactionType.BILLBOARDS);
    }
}
