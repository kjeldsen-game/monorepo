package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.IncomeEventWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.stereotype.Component;

import static org.mockito.Mockito.mock;

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
    public void when_we_make_a_sponsors_income_with_conservative() {
        Team.TeamId teamId = Team.TeamId.generate();
        Team.Economy.IncomeMode mode = Team.Economy.IncomeMode.CONSERVATIVE;
        Integer wins = 5;

        annualIncomeSponsorUseCase.income(teamId, mode, wins);
    }

    @Test
    public void when_we_make_a_sponsors_income_with_moderate() {
        Team.TeamId teamId = Team.TeamId.generate();
        Team.Economy.IncomeMode mode = Team.Economy.IncomeMode.MODERATE;
        Integer wins = 5;

        annualIncomeSponsorUseCase.income(teamId, mode, wins);
    }

    @Test
    public void when_we_make_a_sponsors_income_with_aggressive() {
        Team.TeamId teamId = Team.TeamId.generate();
        Team.Economy.IncomeMode mode = Team.Economy.IncomeMode.CONSERVATIVE;
        Integer wins = 5;

        annualIncomeSponsorUseCase.income(teamId, mode, wins);
    }
}
