package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.events.ExpenseEvent;
import com.kjeldsen.player.domain.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PaySalariesTeamUseCaseTest {

    final private TeamReadRepository teamReadRepository = Mockito.mock(TeamReadRepository.class);
    final private TeamWriteRepository teamWriteRepository = Mockito.mock(TeamWriteRepository.class);
    final private PlayerReadRepository playerReadRepository = Mockito.mock(PlayerReadRepository.class);
    final private ExpenseEventWriteRepository expenseEventWriteRepository = Mockito.mock(ExpenseEventWriteRepository.class);
    final private GeneratePlayersUseCase generatePlayersUseCase = Mockito.mock(GeneratePlayersUseCase.class);
    final private PaySalariesTeamUseCase paySalariesTeamUseCase = new PaySalariesTeamUseCase(teamReadRepository, teamWriteRepository, playerReadRepository, expenseEventWriteRepository);

    @Test
    @DisplayName("should_throw_exception_when_team_does_not_exist")
    public void should_throw_exception_when_team_does_not_exist(){
        Team.TeamId teamId = Team.TeamId.of("exampleTeamId");
        when(teamReadRepository.findById(teamId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paySalariesTeamUseCase.pay(teamId);
    });
        assertEquals("Team not found", exception.getMessage());
        verifyNoInteractions(playerReadRepository, expenseEventWriteRepository, teamWriteRepository);
    }

    @Test
    @DisplayName("should pay salary to players with the given teamId, balance and salaries")
    public void should_pay_salary_to_players_with_the_given_teamId_balance_and_salaries(){
        Team.TeamId newTeamId = Team.TeamId.generate();
        List<Player> players = generatePlayersUseCase.generate(50, newTeamId);
        Team team = Team.builder()
                .id(newTeamId)
                .userId("exampleUserId")
                .name("exampleTeamName")
                .cantera(Team.Cantera.builder()
                        .score(0.0)
                        .economyLevel(0)
                        .traditionLevel(0)
                        .buildingsLevel(0)
                        .build())
                .economy(Team.Economy.builder()
                        .balance(BigDecimal.valueOf(80000))
                        .build())
                .build();

        players.forEach(player -> player.setEconomy(Player.Economy.builder().salary(BigDecimal.valueOf(1000)).build()));

        when(playerReadRepository.findByTeamId(newTeamId)).thenReturn(players);
        when(teamReadRepository.findById(newTeamId)).thenReturn(Optional.of(team));

        paySalariesTeamUseCase.pay(newTeamId);

        BigDecimal expectedBalanceAfterPayment = BigDecimal.valueOf(80000).subtract(BigDecimal.valueOf(1000 * players.size()));
        Assertions.assertEquals(expectedBalanceAfterPayment, team.getEconomy().getBalance());
        verify(expenseEventWriteRepository, times(players.size())).save(any(ExpenseEvent.class));
        verify(teamWriteRepository, times(1)).save(team);

    }
}
