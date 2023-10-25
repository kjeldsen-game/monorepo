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
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class AnnualIncomeSponsorUseCaseTest {

    private final TeamReadRepository teamReadRepository = mock(TeamReadRepository.class);
    private final IncomeEventWriteRepository incomeEventWriteRepository = mock(IncomeEventWriteRepository.class);
    private final TeamWriteRepository teamWriteRepository = mock(TeamWriteRepository.class);
    private final String TEAM_NAME = "TeamName";

    private final AnnualIncomeSponsorUseCase annualIncomeSponsorUseCase = new AnnualIncomeSponsorUseCase(teamReadRepository, incomeEventWriteRepository, teamWriteRepository);

    @Test
    public void when_we_make_a_sponsors_income_generate_an_income_event() {
        Team.TeamId teamId = Team.TeamId.generate();
        Team.Economy.IncomeMode mode = Team.Economy.IncomeMode.CONSERVATIVE;
        Integer wins = 5;

        when(teamReadRepository.findById(teamId)).thenReturn(Optional.of(getTeam(teamId)));

        annualIncomeSponsorUseCase.income(teamId, mode, wins);

        verify(incomeEventWriteRepository)
            .save(
                argThat(income -> income.getTeamId().equals(teamId)
                    && income.getIncomePeriodicity().equals(Team.Economy.IncomePeriodicity.ANNUAL)
                    && income.getIncomeType().equals(Team.Economy.IncomeType.SPONSOR)
                    && income.getIncomeMode().equals(Team.Economy.IncomeMode.CONSERVATIVE)
                )
            );
    }

    @Test
    public void when_we_make_a_sponsors_income_with_conservative_right_amount() {
        Team.TeamId teamId = Team.TeamId.generate();
        Team.Economy.IncomeMode mode = Team.Economy.IncomeMode.CONSERVATIVE;
        Integer wins = 5;
        BigDecimal expectedAmount = BigDecimal.valueOf(1_000_000).add(BigDecimal.valueOf(wins * 100_000));

        when(teamReadRepository.findById(teamId)).thenReturn(Optional.of(getTeam(teamId)));

        annualIncomeSponsorUseCase.income(teamId, mode, wins);


        Assertions.assertThat(annualIncomeSponsorUseCase.getAmount(mode, wins)).isEqualTo(expectedAmount);
    }

    @Test
    public void when_we_make_a_sponsors_income_with_moderate_right_amount() {
        Team.TeamId teamId = Team.TeamId.generate();
        Team.Economy.IncomeMode mode = Team.Economy.IncomeMode.MODERATE;
        Integer wins = 5;
        BigDecimal expectedAmount = BigDecimal.valueOf(750_000).add(BigDecimal.valueOf(wins * 200_000));

        when(teamReadRepository.findById(teamId)).thenReturn(Optional.of(getTeam(teamId)));

        annualIncomeSponsorUseCase.income(teamId, mode, wins);


        Assertions.assertThat(annualIncomeSponsorUseCase.getAmount(mode, wins)).isEqualTo(expectedAmount);
    }

    @Test
    public void when_we_make_a_sponsors_income_with_an_aggressive_right_amount() {
        Team.TeamId teamId = Team.TeamId.generate();
        Team.Economy.IncomeMode mode = Team.Economy.IncomeMode.AGGRESSIVE;
        Integer wins = 5;
        BigDecimal expectedAmount = BigDecimal.valueOf(500_000).add(BigDecimal.valueOf(wins * 300_000));

        when(teamReadRepository.findById(teamId)).thenReturn(Optional.of(getTeam(teamId)));

        annualIncomeSponsorUseCase.income(teamId, mode, wins);


        Assertions.assertThat(annualIncomeSponsorUseCase.getAmount(mode, wins)).isEqualTo(expectedAmount);
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
