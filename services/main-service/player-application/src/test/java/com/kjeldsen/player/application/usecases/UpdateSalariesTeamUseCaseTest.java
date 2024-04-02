package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UpdateSalariesTeamUseCaseTest {

    private final TeamReadRepository teamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final PlayerWriteRepository playerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    private final PlayerReadRepository playerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final GeneratePlayersUseCase generatePlayersUseCase = Mockito.mock(GeneratePlayersUseCase.class);
    private final UpdateSalariesTeamUseCase updateSalariesTeamUseCase = new UpdateSalariesTeamUseCase(teamReadRepository, playerWriteRepository, playerReadRepository);

    @Test
    @DisplayName("should_throw_exception_when_team_does_not_exist")
    public void should_throw_exception_when_team_does_not_exist(){
        Team.TeamId teamId = Team.TeamId.of("exampleTeamId");
        when(teamReadRepository.findById(teamId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            updateSalariesTeamUseCase.update(teamId);
        });
        assertEquals("Team not found", exception.getMessage());
        verifyNoInteractions(playerReadRepository, playerWriteRepository, playerReadRepository);
    }

    @Test
    @DisplayName("should update the salaries of the players with the given team id ")
    public void should_update_the_salaries_of_the_players_with_the_given_team_id(){
        Team.TeamId newTeamId = Team.TeamId.generate();
        List<Player> players = generatePlayersUseCase.generate(50, newTeamId);
        Team team = Team.builder()
                .id(newTeamId)
                .userId("exampleUserId")
                .name("exampleName")
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

        when(playerReadRepository.findByTeamId(newTeamId)).thenReturn(players);
        when(teamReadRepository.findById(newTeamId)).thenReturn(Optional.of(team));
        updateSalariesTeamUseCase.update(newTeamId);
        players.forEach(player -> verify(player, times(1)).negotiateSalary());
        verify(playerWriteRepository, times(players.size())).save(any());

    }
}
