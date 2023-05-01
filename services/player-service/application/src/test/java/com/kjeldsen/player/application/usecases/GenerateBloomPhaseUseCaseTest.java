package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingBloomEventWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static com.kjeldsen.player.domain.PlayerPosition.MIDDLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GenerateBloomPhaseUseCaseTest {

    private final PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);

    private final PlayerTrainingBloomEventWriteRepository mockedPlayerTrainingBloomEventWriteRepository = Mockito.mock(PlayerTrainingBloomEventWriteRepository.class);

    private final GenerateBloomPhaseUseCase generateBloomPhaseUseCase = new GenerateBloomPhaseUseCase(mockedPlayerReadRepository, mockedPlayerTrainingBloomEventWriteRepository);

    @Test
    @DisplayName("Generate inside GenerateBloomPhaseUseCasePhase should throw if player not found.")
    void generate_should_throw_exception_if_player_not_found() {

        // Arrange
        int bloomYears = 1;
        int bloomSpeed = 1;
        int bloomStart = 1;
        Player.PlayerId playerId = new Player.PlayerId("test");

        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> generateBloomPhaseUseCase.generate(bloomYears, bloomSpeed, bloomStart, playerId));
    }

    @Test
    @DisplayName("Generate should call generateAndStoreEventOfBloomPhase and save to repository.")
    void generate_should_call_generateAndStoreEventOfBloomPhase_and_save_to_repository() {

        // Arrange
        int bloomYearsOn = 3;
        int bloomSpeed = 100;
        int bloomStartAge = 23;
        Player.PlayerId playerId = Player.PlayerId.generate();

        Player playerTest = getPlayer(playerId);

        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.ofNullable(playerTest));

        // Act
        generateBloomPhaseUseCase.generate(bloomYearsOn, bloomSpeed, bloomStartAge, playerId);

        // Assert
        ArgumentCaptor<PlayerTrainingBloomEvent> argumentCaptor = ArgumentCaptor.forClass(PlayerTrainingBloomEvent.class);
        verify(mockedPlayerTrainingBloomEventWriteRepository, Mockito.times(1))
            .save(argumentCaptor.capture());

        PlayerTrainingBloomEvent playerTrainingBloomEvent = argumentCaptor.getValue();

        assertEquals(playerId, playerTrainingBloomEvent.getPlayerId());
        assertEquals(bloomYearsOn, playerTrainingBloomEvent.getYearsOn());
        assertEquals(bloomStartAge, playerTrainingBloomEvent.getBloomStartAge());
        assertEquals(bloomSpeed, playerTrainingBloomEvent.getBloomSpeed());
        verify(mockedPlayerReadRepository, times(1)).findOneById(playerId);
        verify(mockedPlayerTrainingBloomEventWriteRepository, times(1)).save(any());
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
