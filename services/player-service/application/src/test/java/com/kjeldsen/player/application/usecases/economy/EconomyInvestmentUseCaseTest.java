package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.application.usecases.cantera.CanteraEconomyInvestmentUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.EconomyInvestmentEventWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class EconomyInvestmentUseCaseTest {

    private static final BigDecimal MAX_KEY_VALUE_INVESTMENT_POINTS = BigDecimal.valueOf(5);
    private static final BigDecimal KEY_VALUE_INVESTMENT_AMOUNT = BigDecimal.valueOf(250_000);
    private static final BigDecimal MAX_INVESTMENT_AMOUNT = BigDecimal.valueOf(1_000_000);

    private final TeamReadRepository teamReadRepository = mock(TeamReadRepository.class);
    private final EconomyInvestmentEventWriteRepository economyInvestmentEventWriteRepository = mock(EconomyInvestmentEventWriteRepository.class);
    private final CanteraEconomyInvestmentUseCase canteraEconomyInvestmentUsecase = mock(CanteraEconomyInvestmentUseCase.class);
    private final String TEAM_NAME = "TeamName";

    private final EconomyInvestmentUseCase economyInvestmentUseCase = new EconomyInvestmentUseCase(teamReadRepository, economyInvestmentEventWriteRepository, canteraEconomyInvestmentUsecase);

    @Test
    public void when_we_make_a_investment_generate_an_economy_investment_event() {
        Team.TeamId teamId = Team.TeamId.generate();
        BigDecimal amount = BigDecimal.valueOf(1000);

        when(teamReadRepository.findById(teamId)).thenReturn(Optional.of(getTeam(teamId)));

        economyInvestmentUseCase.invest(teamId, amount);

        verify(economyInvestmentEventWriteRepository)
            .save(
                argThat(investmentEvent -> investmentEvent.getTeamId().equals(teamId)
                    && investmentEvent.getAmount().equals(amount)
                )
            );
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
                .balance(BigDecimal.valueOf(1000000))
                .stadium(Team.Economy.Stadium.builder()
                    .seats(10000)
                    .build())
                .build())
            .build();
    }

}
