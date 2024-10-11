package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.application.usecases.CreateTransactionUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
@RequiredArgsConstructor
public class MerchandiseIncomeUseCase {

    private final TeamReadRepository teamReadRepository;
    private final CreateTransactionUseCase createTransactionUseCase;

    public void income(Team.TeamId teamId, Integer homeAttendanceCount) {
        log.info("MerchandiseIncomeUseCase team {}", teamId);

        Team team = teamReadRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        if (homeAttendanceCount < 0 ) {
            throw new IllegalArgumentException("Attendance count cannot be negative");
        }

        BigDecimal amount = BigDecimal.valueOf(homeAttendanceCount).multiply(BigDecimal.valueOf(team.getEconomy()
            .getPrices().get(Team.Economy.PricingType.MERCHANDISE))).multiply(BigDecimal.valueOf(5))
            .divide(BigDecimal.valueOf(100));

        createTransactionUseCase.create(teamId, amount, Transaction.TransactionType.MERCHANDISE);
    }
}
