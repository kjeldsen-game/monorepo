package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
@RequiredArgsConstructor
public class SponsorIncomeUsecase {
    private final CreateTransactionUseCase createTransactionUseCase;

    public void incomeWeekly(Team.TeamId teamId, Team.Economy.IncomeMode mode, Integer wins) {
        income(teamId, mode, wins, Team.Economy.IncomePeriodicity.WEEKLY);
    }

    public void incomeAnnual(Team.TeamId teamId, Team.Economy.IncomeMode mode, Integer wins) {
        income(teamId, mode, wins, Team.Economy.IncomePeriodicity.ANNUAL);
    }

    private void income(Team.TeamId teamId, Team.Economy.IncomeMode mode, Integer wins, Team.Economy.IncomePeriodicity incomePeriodicity) {
        log.info("SponsorIncomeEvent periodicity {} team {} with {} mode and {} wins", incomePeriodicity, teamId, mode, wins);
        BigDecimal amount = getAmount(mode, wins, incomePeriodicity);
        createTransactionUseCase.create(teamId, amount, Transaction.TransactionType.SPONSOR);
    }

    private BigDecimal getAmount(Team.Economy.IncomeMode mode, Integer wins, Team.Economy.IncomePeriodicity periodicity) {
        return switch (periodicity) {
            case WEEKLY -> switch (mode) {
                case CONSERVATIVE -> BigDecimal.valueOf(100_000);
                case MODERATE -> BigDecimal.valueOf(50_000).add(BigDecimal.valueOf(wins * 100_000));
                case AGGRESSIVE -> BigDecimal.valueOf(wins * 200_000);
            };
            case ANNUAL -> switch (mode) {
                case CONSERVATIVE -> BigDecimal.valueOf(1_000_000).add(BigDecimal.valueOf(wins * 100_000));
                case MODERATE -> BigDecimal.valueOf(750_000).add(BigDecimal.valueOf(wins * 200_000));
                case AGGRESSIVE -> BigDecimal.valueOf(500_000).add(BigDecimal.valueOf(wins * 300_000));
            };
        };
    }
}
