package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.exceptions.TeamNotFoundException;
import com.kjeldsen.player.application.usecases.economy.UpdateSalariesTeamUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.kjeldsen.player.application.testdata.*;

public class UpdateSalariesTeamUseCaseTest {

    private final PlayerWriteRepository playerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    private final PlayerReadRepository playerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final UpdateSalariesTeamUseCase updateSalariesTeamUseCase = new UpdateSalariesTeamUseCase(playerWriteRepository,
        playerReadRepository);

    private Team.TeamId mockedTeamId;

    @BeforeEach
    public void setUp() {
        mockedTeamId = TestData.generateTestTeamId();
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
    }

    @Test
    @DisplayName("Should throw exception when team does not have any players")
    public void should_throw_exception_when_team_does_not_have_any_players(){
        when(playerReadRepository.findByTeamId(mockedTeamId)).thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(TeamNotFoundException.class, () -> {
            updateSalariesTeamUseCase.update(mockedTeamId);
        });
        assertEquals("Team not found!", exception.getMessage());
        verify(playerReadRepository, times(1)).findByTeamId(mockedTeamId);
        verifyNoInteractions(playerWriteRepository);
    }

    @Test
    @DisplayName("Should update the salaries of the players with the given team id ")
    public void should_update_the_salaries_of_the_players_with_the_given_team_id(){
        // Created player with 10 skill points for each multiplier, after creation salary is null
        Player mockedPlayer = generateMockedPlayer();

        when(playerReadRepository.findByTeamId(mockedTeamId)).thenReturn(Collections.singletonList(mockedPlayer));

        updateSalariesTeamUseCase.update(mockedTeamId);
        assertEquals(mockedPlayer.getEconomy().getSalary(), BigDecimal.valueOf(92300).setScale(2));
        assertNotNull(mockedPlayer.getEconomy().getSalary());
        verify(playerReadRepository, times(1)).findByTeamId(mockedTeamId);
    }

    private Player generateMockedPlayer() {
        Map<PlayerSkill, PlayerSkills> skillMap = new HashMap<>();
        ArrayList<Integer> skillPointsList = new ArrayList<>(Arrays.asList(20,30,40,50,60,70,80,90,100));
        int index = 0;
        for( PlayerSkill playerSkill : PlayerSkill.values()) {
            skillMap.put(playerSkill, PlayerSkills.builder().potential(PlayerSkills.MAX_SKILL_VALUE).actual(skillPointsList.get(index)).build());
            index++;
            if (index == skillPointsList.size()) {
                break;
            }
        }
        return Player.builder().id(Player.PlayerId.generate()).economy(Player.Economy.builder().build()).actualSkills(skillMap).build();
    }
}
