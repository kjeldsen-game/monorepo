package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static com.kjeldsen.player.domain.PlayerPosition.MIDDLE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GenerateDeclinePhaseUseCaseTest {

    private final PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);

    private final PlayerTrainingDeclineEventWriteRepository mockedPlayerTrainingDeclineEventWriteRepository = Mockito.mock(PlayerTrainingDeclineEventWriteRepository.class);

    private final GenerateDeclinePhaseUseCase generateDeclinePhaseUseCase = new GenerateDeclinePhaseUseCase(mockedPlayerReadRepository, mockedPlayerTrainingDeclineEventWriteRepository);

    @Test
    @DisplayName("Generate should throw if player not found.")
    void generate_should_throw_exception_if_player_not_found() {
        // Arrange
        int declineYears = 1;
        int declineStart = 1;
        Player.PlayerId playerId = new Player.PlayerId("wachisney");

        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> generateDeclinePhaseUseCase.generate(declineYears, declineStart, playerId));
    }

    @Test
    @DisplayName("Generate should call generateAndStoreEventOfDeclinePhase and save to repository.")
    void generate_should_call_generateAndStoreEventOfDeclinePhase_and_save_to_repository() {

        // Arrange
        int declineYears = 3;
        int declineStart = 23;
        Player.PlayerId playerId = Player.PlayerId.generate();
        Player wachisneyRgz = getPlayer(playerId);
        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.ofNullable(wachisneyRgz));

        // Act
        generateDeclinePhaseUseCase.generate(declineYears, declineStart, playerId);

        // Assert
        verify(mockedPlayerReadRepository, times(1)).findOneById(playerId);
        verify(mockedPlayerTrainingDeclineEventWriteRepository, times(1)).save(any());
    }

    private Player getPlayer(Player.PlayerId playerId) {
        return Player.builder()
            .id(playerId)
            .name(PlayerProvider.name())
            .age(PlayerProvider.age())
            .position(PlayerPosition.MIDDLE)
            .actualSkills(PlayerProvider.skillsBasedOnTendency(PlayerPositionTendency.getDefault(MIDDLE), 200))
            .build();
    }
}
