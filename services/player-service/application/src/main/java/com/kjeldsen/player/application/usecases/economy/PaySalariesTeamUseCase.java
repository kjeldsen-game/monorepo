package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.events.domain.EventId;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.ExpenseEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.ExpenseEventWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import com.kjeldsen.player.domain.repositories.player.PlayerReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

        playerReadRepository.findByTeamId(teamId)
            .forEach(player -> {
                team.getEconomy().decreaseBalance(player.getEconomy().getSalary());

                expenseEventWriteRepository.save(
                    ExpenseEvent.builder()
                        .id(EventId.generate())
                        .occurredAt(InstantProvider.now())
                        .teamId(teamId)
                        .playerId(player.getId())
                        .amount(player.getEconomy().getSalary())
                        .expenseType(Team.Economy.ExpenseType.PLAYER_SALARY)
                        .build());
            });

        teamWriteRepository.save(team);
    }
}
