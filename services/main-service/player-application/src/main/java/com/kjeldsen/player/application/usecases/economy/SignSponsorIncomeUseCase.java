package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class SignSponsorIncomeUseCase {

    private final TeamReadRepository teamReadRepository;
    private final TeamWriteRepository teamWriteRepository;
    private final CreateTransactionUseCase createTransactionUseCase;

    public void sign(Team.TeamId teamId, Team.Economy.IncomePeriodicity periodicity, Team.Economy.IncomeMode mode) {
        Team team = teamReadRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        if (team.getEconomy().getSponsors().get(periodicity) != null) {
            throw new RuntimeException("Sponsor already exists");
        }

        team.getEconomy().getSponsors().put(periodicity, mode);

        BigDecimal amount = getAmount(mode, periodicity, false);
        createTransactionUseCase.create(teamId, amount, Transaction.TransactionType.SPONSOR);
        teamWriteRepository.save(team);
    }

    public void processBonus(Team.TeamId teamId) {
        Team team = teamReadRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        Map<Team.Economy.IncomePeriodicity, Team.Economy.IncomeMode> sponsors =  team.getEconomy().getSponsors();
        sponsors.forEach((periodicity, mode) -> {
            if (mode != null) {
                BigDecimal amount = getAmount(mode, periodicity, true);
                createTransactionUseCase.create(teamId, amount, Transaction.TransactionType.SPONSOR);
            }
        });
    }

    private BigDecimal getAmount(Team.Economy.IncomeMode mode, Team.Economy.IncomePeriodicity periodicity, boolean isWinBonus) {
        return switch (periodicity) {
            case WEEKLY -> switch (mode) {
                case CONSERVATIVE -> isWinBonus ? BigDecimal.ZERO : BigDecimal.valueOf(100_000);
                case MODERATE -> isWinBonus ? BigDecimal.valueOf(100_000): BigDecimal.valueOf(50_000);
                case AGGRESSIVE -> isWinBonus ? BigDecimal.valueOf(200_000) : BigDecimal.ZERO;
            };
            case ANNUAL -> switch (mode) {
                case CONSERVATIVE -> isWinBonus ? BigDecimal.valueOf(100_000) : BigDecimal.valueOf(1_000_000);
                case MODERATE -> isWinBonus ? BigDecimal.valueOf(200_000) : BigDecimal.valueOf(750_000);
                case AGGRESSIVE -> isWinBonus ? BigDecimal.valueOf(300_000) : BigDecimal.valueOf(500_000);
            };
        };
    }

}
