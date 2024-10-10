package com.kjeldsen.player.application.usecases;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.events.ExpenseEvent;
import com.kjeldsen.player.domain.events.TransactionEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaySalariesTeamUseCase {

    private final PlayerReadRepository playerReadRepository;
    private final CreateTransactionUseCase createTransactionUseCase;

    @Transactional
    public void pay(Team.TeamId teamId) {
        log.info("PaySalariesTeamUseCase team with id {} ", teamId);

        BigDecimal totalSalaries = playerReadRepository.findByTeamId(teamId)
                .stream()
                .map(player -> player.getEconomy().getSalary())
                .peek(salary -> {
                    if (salary.compareTo(BigDecimal.ZERO) < 0) {
                        throw new IllegalArgumentException("Salary cannot be negative");
                    }
                }).reduce(BigDecimal.ZERO, BigDecimal::add);

        createTransactionUseCase.create(teamId, totalSalaries, Transaction.TransactionType.PLAYER_WAGE);
    }
}
