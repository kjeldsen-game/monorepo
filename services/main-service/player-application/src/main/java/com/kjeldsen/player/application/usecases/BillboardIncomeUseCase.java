package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
@RequiredArgsConstructor
public class BillboardIncomeUseCase {

    private final TeamReadRepository teamReadRepository;
    private final TeamWriteRepository teamWriteRepository;
    private final CreateTransactionUseCase createTransactionUseCase;

    public void incomeWinBonus(Team.TeamId teamId, Integer wins) {
        log.info("BillboardIncomeUseCase for team {}", teamId);

        Team team = teamReadRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        try {
            Team.Economy.IncomeMode weeklyIncomeMode = team.getEconomy().getBillboards().get(Team.Economy.IncomePeriodicity.WEEKLY);
            if (weeklyIncomeMode == null) {
                throw new RuntimeException("Weekly income mode not found");
            }
            BigDecimal weeklyAmount = getAmountPerWin(weeklyIncomeMode, wins, Team.Economy.IncomePeriodicity.WEEKLY);

            if (weeklyAmount.compareTo(BigDecimal.ZERO) > 0)
                createTransactionUseCase.create(teamId, weeklyAmount, Transaction.TransactionType.BILLBOARDS);
        } catch (Exception e) {
            log.info("Weekly income mode not found!");
        }

        try {
            Team.Economy.IncomeMode annualIncomeMode = team.getEconomy().getBillboards().get(Team.Economy.IncomePeriodicity.ANNUAL);
            if (annualIncomeMode == null) {
                throw new RuntimeException("Weekly income mode not found");
            }
            BigDecimal annualAmount = getAmountPerWin(annualIncomeMode, wins, Team.Economy.IncomePeriodicity.ANNUAL);
            if (annualAmount.compareTo(BigDecimal.ZERO) > 0)
                createTransactionUseCase.create(teamId, annualAmount, Transaction.TransactionType.BILLBOARDS);
        } catch (Exception e) {
            log.info("Annual income mode not found!");
        }
    }

    public void incomeSelection(Team.TeamId teamId, Team.Economy.IncomeMode mode, Team.Economy.IncomePeriodicity periodicity) {
        log.info("BillboardIncomeUseCase periodicity {} team {} mode {}", periodicity, teamId, mode);

        Team team = teamReadRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        team.getEconomy().getBillboards().put(periodicity, mode);
        BigDecimal amount = getAmount(mode, periodicity);
        if (amount.compareTo(BigDecimal.ZERO) > 0)
            createTransactionUseCase.create(teamId, amount, Transaction.TransactionType.BILLBOARDS);

        teamWriteRepository.save(team);
    }

    private BigDecimal getAmount(Team.Economy.IncomeMode mode, Team.Economy.IncomePeriodicity periodicity) {
        return switch (periodicity) {
            case WEEKLY -> switch (mode) {
                case CONSERVATIVE -> BigDecimal.valueOf(100_000);
                case MODERATE -> BigDecimal.valueOf(50_000);
                case AGGRESSIVE -> BigDecimal.ZERO;
            };
            case ANNUAL -> switch (mode) {
                case CONSERVATIVE -> BigDecimal.valueOf(1_000_000);
                case MODERATE -> BigDecimal.valueOf(750_000);
                case AGGRESSIVE -> BigDecimal.valueOf(500_000);
            };
        };
    }

    private BigDecimal getAmountPerWin(Team.Economy.IncomeMode mode, Integer wins, Team.Economy.IncomePeriodicity periodicity) {
        return switch (periodicity) {
            case WEEKLY -> switch (mode) {
                case CONSERVATIVE -> BigDecimal.ZERO;
                case MODERATE -> BigDecimal.valueOf(wins * 100_000);
                case AGGRESSIVE -> BigDecimal.valueOf(wins * 200_000);
            };
            case ANNUAL -> switch (mode) {
                case CONSERVATIVE -> BigDecimal.valueOf(wins * 100_000);
                case MODERATE -> BigDecimal.valueOf(wins * 200_000);
                case AGGRESSIVE -> BigDecimal.valueOf(wins * 300_000);
            };
        };
    }
}
