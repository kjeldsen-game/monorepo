package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.exceptions.IllegalAttendanceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
@RequiredArgsConstructor
public class RestaurantIncomeUseCase {

    private final GetTeamUseCase getTeamUseCase;
    private final CreateTransactionUseCase createTransactionUseCase;

    public void income(Team.TeamId teamId, Integer attendanceCount) {
        log.info("RestaurantIncomeUseCase team {}", teamId);

        Team team = getTeamUseCase.get(teamId);

        if (attendanceCount < 0 ) {
            throw new IllegalAttendanceException();
        }

        BigDecimal amount = BigDecimal.valueOf(attendanceCount).multiply(BigDecimal.valueOf(team.getEconomy()
            .getPrices().get(Team.Economy.PricingType.RESTAURANT))).multiply(BigDecimal.valueOf(10))
            .divide(BigDecimal.valueOf(100));

        createTransactionUseCase.create(teamId, amount, Transaction.TransactionType.RESTAURANT);
    }
}
