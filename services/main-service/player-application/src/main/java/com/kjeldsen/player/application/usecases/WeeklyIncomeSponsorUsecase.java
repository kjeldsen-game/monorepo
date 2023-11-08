package com.kjeldsen.player.application.usecases;

import com.kjeldsen.events.domain.EventId;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.IncomeEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.IncomeEventWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Component
public class WeeklyIncomeSponsorUsecase {

    private final TeamReadRepository teamReadRepository;
    private final IncomeEventWriteRepository incomeEventWriteRepository;
    private final TeamWriteRepository teamWriteRepository;

    public void income(Team.TeamId teamId, Team.Economy.IncomeMode mode, Integer wins) {
        log.info("WeeklyIncomeSponsorUsecase team {} with {} mode and {} wins", teamId, mode, wins);

        Team team = teamReadRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        BigDecimal amount = getAmount(mode, wins);

        IncomeEvent incomeEvent = IncomeEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .teamId(teamId)
            .incomePeriodicity(Team.Economy.IncomePeriodicity.WEEKLY)
            .incomeType(Team.Economy.IncomeType.SPONSOR)
            .incomeMode(mode)
            .amount(amount)
            .wins(wins)
            .build();

        incomeEventWriteRepository.save(incomeEvent);

        team.getEconomy().increaseBalance(amount);

        teamWriteRepository.save(team);
    }

    private BigDecimal getAmount(Team.Economy.IncomeMode mode, Integer wins) {
        return switch (mode) {
            case CONSERVATIVE -> BigDecimal.valueOf(100_000);
            case MODERATE -> BigDecimal.valueOf(50_000).add(BigDecimal.valueOf(wins * 100_000));
            case AGGRESSIVE -> BigDecimal.valueOf(wins * 200_000);
        };
    }

}
