package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.usecases.economy.CreateTransactionUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaySalariesTeamUseCase {

    private final PlayerReadRepository playerReadRepository;
    private final CreateTransactionUseCase createTransactionUseCase;

    @Transactional
    public void pay(Team.TeamId teamId) {
        log.info("PaySalariesTeamUseCase team with id {} ", teamId);
        playerReadRepository.findByTeamId(teamId)
            .forEach(player -> {
                BigDecimal salary = player.getEconomy().getSalary();
                if (salary.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException("Salary cannot be negative");
                }
                createTransactionUseCase.create(teamId, salary.negate(), player.getId().value());
            });
    }
}
