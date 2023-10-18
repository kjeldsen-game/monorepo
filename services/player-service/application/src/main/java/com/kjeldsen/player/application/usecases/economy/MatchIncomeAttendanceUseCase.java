package com.kjeldsen.player.application.usecases.economy;

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
public class MatchIncomeAttendanceUseCase {

    private final TeamReadRepository teamReadRepository;
    private final IncomeEventWriteRepository incomeEventWriteRepository;
    private final TeamWriteRepository teamWriteRepository;

    public void income(Team.TeamId teamId, Integer spectators, Double seatPrice) {
        log.info("MatchIncomeAttendanceUsecase team {} with {} spectators and {} seat price.", teamId, spectators, seatPrice);
        Team team = teamReadRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        BigDecimal amount = BigDecimal.valueOf(calculateMatchRevenue(team, spectators, seatPrice));

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

    public Double calculateMatchRevenue(Team team, Integer spectators, Double seatPrice) {
        if (team.getEconomy().getStadium().getSeats() < spectators) {
            throw new IllegalArgumentException("Your stadium allows: " + team.getEconomy().getStadium().getSeats() + " spectators. Try again.");
        }

        if (spectators < 0) {
            throw new IllegalArgumentException("You cannot have less than 0 spectators. Try again.");
        }

        return seatPrice * spectators;
    }

}
