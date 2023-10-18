package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.IncomeEventWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class MatchIncomeAttendanceUseCaseTest {

    private final TeamReadRepository teamReadRepository = mock(TeamReadRepository.class);
    private final IncomeEventWriteRepository incomeEventWriteRepository = mock(IncomeEventWriteRepository.class);
    private final TeamWriteRepository teamWriteRepository = mock(TeamWriteRepository.class);
    private final String TEAM_NAME = "TeamName";

    private final MatchIncomeAttendanceUseCase matchIncomeAttendanceUseCaseTest = new MatchIncomeAttendanceUseCase(teamReadRepository, incomeEventWriteRepository, teamWriteRepository);


    @Test
    public void when_we_play_a_match_we_get_the_income() {
        Integer spectators = 1000;
        Double seatPrice = 20.0;
        Team.TeamId teamId = Team.TeamId.generate();

        BigDecimal balance = BigDecimal.valueOf(spectators * seatPrice);

        when(teamReadRepository.findById(teamId)).thenReturn(Optional.of(getTeam(teamId)));

        matchIncomeAttendanceUseCaseTest.income(teamId, spectators, seatPrice);

        verify(teamWriteRepository)
            .save(
                argThat(team -> !Objects.isNull(team.getId())
                    && team.getEconomy().getBalance().equals(balance)
                )
            );
    }

    @Test
    public void when_we_play_a_match_with_more_spectators_throw_an_error() {
        Integer spectators = 11000;
        Double seatPrice = 20.0;
        Team.TeamId teamId = Team.TeamId.generate();

        when(teamReadRepository.findById(teamId)).thenReturn(Optional.of(getTeam(teamId)));

        Assertions.assertThatThrownBy(() -> matchIncomeAttendanceUseCaseTest.calculateMatchRevenue(getTeam(teamId), spectators, seatPrice)).isInstanceOf(IllegalArgumentException.class).hasMessage(
            "Your stadium allows: " + getTeam(teamId).getEconomy().getStadium().getSeats() + " spectators. Try again.");
    }

    @Test
    public void when_we_play_a_match_with_less_spectators_throw_an_error() {
        Integer spectators = -1;
        Double seatPrice = 20.0;
        Team.TeamId teamId = Team.TeamId.generate();

        BigDecimal balance = BigDecimal.valueOf(spectators * seatPrice);

        when(teamReadRepository.findById(teamId)).thenReturn(Optional.of(getTeam(teamId)));

        Assertions.assertThatThrownBy(() -> matchIncomeAttendanceUseCaseTest.calculateMatchRevenue(getTeam(teamId), spectators, seatPrice)).isInstanceOf(IllegalArgumentException.class).hasMessage(
            "You cannot have less than 0 spectators. Try again.");
    }

    private Team getTeam(Team.TeamId teamId) {
        return Team.builder()
            .id(teamId)
            .userId(UUID.randomUUID().toString())
            .name(TEAM_NAME)
            .cantera(Team.Cantera.builder()
                .score(0.0)
                .economyLevel(0)
                .traditionLevel(0)
                .buildingsLevel(0)
                .build())
            .economy(Team.Economy.builder()
                .balance(BigDecimal.ZERO)
                .stadium(Team.Economy.Stadium.builder()
                    .seats(10000)
                    .build())
                .build())
            .build();
    }

}
