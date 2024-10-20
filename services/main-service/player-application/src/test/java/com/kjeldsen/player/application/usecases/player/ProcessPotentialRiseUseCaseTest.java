package com.kjeldsen.player.application.usecases.player;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.generator.PotentialRiseGenerator;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerPotentialRiseEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessPotentialRiseUseCaseTest {

    private static final Integer MAX_AGE = 21;
    @Mock
    private PlayerPotentialRiseEventWriteRepository playerPotentialRiseEventWriteRepository;
    @Mock
    private PlayerReadRepository playerReadRepository;
    @Mock
    private PlayerWriteRepository playerWriteRepository;
    @InjectMocks
    private ProcessPotentialRiseUseCase processPotentialRiseUseCase;

    @Test
    @DisplayName("Should process potential rise usecase for players")
    public void should_process_potential_rise() {
        List<Player> players = TestData.generateTestPlayers(Team.TeamId.of("example"), 5);
        MockedStatic<PotentialRiseGenerator> riseGenerator = Mockito.mockStatic(PotentialRiseGenerator.class);
        players.forEach(player -> {
            player.setAge(PlayerAge.builder().days(12.0).months(2.0).years(20).build());
        });

        when(playerReadRepository.findPlayerUnderAge(MAX_AGE)).thenReturn(players);
        riseGenerator.when(PotentialRiseGenerator::generatePotentialRaise).thenReturn(1,0,3,2,0);

        processPotentialRiseUseCase.process();

        verify(playerReadRepository).findPlayerUnderAge(MAX_AGE);
        riseGenerator.verify(PotentialRiseGenerator::generatePotentialRaise, times(5));
        verify(playerWriteRepository,times(3)).save(any());
        verify(playerPotentialRiseEventWriteRepository, times(3)).save(any());
    }
}