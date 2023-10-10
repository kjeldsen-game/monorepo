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
public class MatchIncomeAttendanceUsecase {
    private final TeamReadRepository teamReadRepository;
    private final IncomeEventWriteRepository incomeEventWriteRepository;
    private final TeamWriteRepository teamWriteRepository;

    public void income(Team.TeamId teamId, Integer spectators) {
        log.info("MatchIncomeAttendanceUsecase team {} with {} mode and {} spectators", teamId, spectators);

        Team team = teamReadRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        BigDecimal amount = BigDecimal.valueOf(calculateMatchRevenue(spectators));

        IncomeEvent incomeEvent = IncomeEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .teamId(teamId)
            .incomePeriodicity(Team.Economy.IncomePeriodicity.MATCH)
            .incomeType(Team.Economy.IncomeType.ATTENDANCE)
            .incomeMode(null)
            .amount(amount)
            .wins(null)
            .build();

        incomeEventWriteRepository.save(incomeEvent);

        team.getEconomy().increaseBalance(amount);

        teamWriteRepository.save(team);
    }

    private Double calculateMatchRevenue(Integer spectators) {
        Double seatPrice = 10.0;
        return seatPrice * spectators;
    }
}
