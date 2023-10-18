package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.application.usecases.GeneratePlayersUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaySalariesTeamUseCaseTest {
    private final String TEAM_NAME = "TeamName";
    private final TeamReadRepository teamReadRepository = mock(TeamReadRepository.class);
    private final ExpenseEventWriteRepository expenseEventWriteRepository = mock(ExpenseEventWriteRepository.class);
    private final TeamWriteRepository teamWriteRepository = mock(TeamWriteRepository.class);
    private final PlayerWriteRepository playerWriteRepository = mock(PlayerWriteRepository.class);
    private final PlayerWriteRepository mockedPlayerWriteRepository = mock(PlayerWriteRepository.class);
    final private PlayerPositionTendencyReadRepository mockedPlayerPositionTendencyReadRepository = mock(
        PlayerPositionTendencyReadRepository.class);
    private final GeneratePlayersUseCase generatePlayersUseCase = new GeneratePlayersUseCase(mockedPlayerWriteRepository,
        mockedPlayerPositionTendencyReadRepository);
    private final PlayerReadRepository playerReadRepository = mock(PlayerReadRepository.class);
    private final PaySalariesTeamUseCase paySalariesTeamUseCase = new PaySalariesTeamUseCase(teamReadRepository, teamWriteRepository, playerReadRepository, expenseEventWriteRepository);

    @Test
    public void take_salaries_from_players_has_an_event_by_each_player() {
        Team.TeamId teamId = Team.TeamId.generate();
        when(mockedPlayerPositionTendencyReadRepository.get(any(PlayerPosition.class)))
            .thenReturn(PlayerPositionTendency.DEFAULT_CENTRE_BACK_TENDENCIES,
                PlayerPositionTendency.DEFAULT_AERIAL_CENTRE_BACK_TENDENCIES,
                PlayerPositionTendency.DEFAULT_FULL_BACK_TENDENCIES,
                PlayerPositionTendency.DEFAULT_FULL_WINGBACK_TENDENCIES,
                PlayerPositionTendency.DEFAULT_DEFENSIVE_MIDFIELDER_TENDENCIES,
                PlayerPositionTendency.DEFAULT_CENTRE_MIDFIELDER_TENDENCIES,
                PlayerPositionTendency.DEFAULT_OFFENSIVE_MIDFIELDER_TENDENCIES,
                PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES,
                PlayerPositionTendency.DEFAULT_AERIAL_FORWARD_TENDENCIES,
                PlayerPositionTendency.DEFAULT_GOALKEEPER_TENDENCIES);

        generatePlayersUseCase.generate(10, teamId);

        ArgumentCaptor<Player> argumentCaptor = ArgumentCaptor.forClass(Player.class);
        verify(mockedPlayerWriteRepository, times(10))
            .save(argumentCaptor.capture());

        List<Player> playersToSave = argumentCaptor.getAllValues();

        when(teamReadRepository.findById(teamId)).thenReturn(Optional.of(getTeam(teamId)));
        when(playerReadRepository.findByTeamId(teamId)).thenReturn(playersToSave);

        paySalariesTeamUseCase.pay(teamId);

        playerReadRepository.findByTeamId(teamId)
            .forEach(player -> {

                verify(expenseEventWriteRepository)
                    .save(
                        argThat(expenseEvent -> expenseEvent.getTeamId().equals(teamId)
                            && expenseEvent.getPlayerId().equals(player.getId())
                            && expenseEvent.getExpenseType().equals(Team.Economy.ExpenseType.PLAYER_SALARY)
                        )
                    );
            });

        Assertions.assertThat(playerReadRepository.findByTeamId(teamId).size()).isEqualTo(10);
    }

    @Test
    public void take_salaries_from_players_decreasing_the_balance_in_right_way() {
        Team.TeamId teamId = Team.TeamId.generate();
        when(mockedPlayerPositionTendencyReadRepository.get(any(PlayerPosition.class)))
            .thenReturn(PlayerPositionTendency.DEFAULT_CENTRE_BACK_TENDENCIES,
                PlayerPositionTendency.DEFAULT_AERIAL_CENTRE_BACK_TENDENCIES,
                PlayerPositionTendency.DEFAULT_FULL_BACK_TENDENCIES,
                PlayerPositionTendency.DEFAULT_FULL_WINGBACK_TENDENCIES,
                PlayerPositionTendency.DEFAULT_DEFENSIVE_MIDFIELDER_TENDENCIES,
                PlayerPositionTendency.DEFAULT_CENTRE_MIDFIELDER_TENDENCIES,
                PlayerPositionTendency.DEFAULT_OFFENSIVE_MIDFIELDER_TENDENCIES,
                PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES,
                PlayerPositionTendency.DEFAULT_AERIAL_FORWARD_TENDENCIES,
                PlayerPositionTendency.DEFAULT_GOALKEEPER_TENDENCIES);

        generatePlayersUseCase.generate(10, teamId);

        ArgumentCaptor<Player> argumentCaptor = ArgumentCaptor.forClass(Player.class);
        verify(mockedPlayerWriteRepository, times(10))
            .save(argumentCaptor.capture());

        List<Player> playersToSave = argumentCaptor.getAllValues();

        when(teamReadRepository.findById(teamId)).thenReturn(Optional.of(getTeam(teamId)));
        Team teamm = teamReadRepository.findById(teamId).orElse(null);
        when(playerReadRepository.findByTeamId(teamId)).thenReturn(playersToSave);

        paySalariesTeamUseCase.pay(teamId);

        BigDecimal balanceTeam = teamm.getEconomy().getBalance();
        playersToSave.forEach(player -> {
            teamm.getEconomy().decreaseBalance(player.getEconomy().getSalary());
        });
        BigDecimal finalBalanceTeam = getTeam(teamId).getEconomy().getBalance();
        Assertions.assertThat(balanceTeam).isLessThan(finalBalanceTeam);
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
