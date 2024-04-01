package com.kjeldsen.player.application.usecases;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.ExpenseEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.ExpenseEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
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

    private final TeamReadRepository teamReadRepository;
    private final TeamWriteRepository teamWriteRepository;
    private final PlayerReadRepository playerReadRepository;
    private final ExpenseEventWriteRepository expenseEventWriteRepository;

    @Transactional
    public void pay(Team.TeamId teamId) {
        log.info("PaySalariesTeamUseCase team with id {} ", teamId);

        Team team = teamReadRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        BigDecimal totalSalaries = playerReadRepository.findByTeamId(teamId)
                .stream().map(player -> player.getEconomy().getSalary())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        if(team.getEconomy().getBalance().compareTo(totalSalaries) < 0){
            throw new IllegalStateException("Insufficient balance to pay salaries");
        }

        playerReadRepository.findByTeamId(teamId)
            .forEach(player -> {
                Optional<Player> optionalPlayer = playerReadRepository.findOneById(player.getId());

                if(optionalPlayer.isEmpty()){
                    throw new RuntimeException("Player with id" + player.getId() + "not found");
                }

                BigDecimal salary = player.getEconomy().getSalary();
                if(salary.compareTo(BigDecimal.ZERO) < 0){
                    throw new IllegalArgumentException("Salary can not be negative");
                }

                team.getEconomy().decreaseBalance(salary);

                expenseEventWriteRepository.save(
                    ExpenseEvent.builder()
                        .id(EventId.generate())
                        .occurredAt(InstantProvider.now())
                        .teamId(teamId)
                        .playerId(player.getId())
                        .amount(salary)
                        .expenseType(Team.Economy.ExpenseType.PLAYER_SALARY)
                        .build());
            });

        teamWriteRepository.save(team);
    }
}
