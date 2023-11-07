package com.kjeldsen.player.application.usecases.training;

import com.kjeldsen.events.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.player.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.player.PlayerWriteRepository;
import com.kjeldsen.player.domain.repositories.training.PlayerTrainingBloomEventWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GenerateBloomPhaseUseCaseTest {

    private static final int BLOOM_YEARS_ON = 3;
    private static final int BLOOM_SPEED = 100;
    private static final int BLOOM_START_AGE = 23;
    private static final Player.PlayerId PLAYER_ID = mock(Player.PlayerId.class);
    private static final EventId EVENT_ID = EventId.from("event-id");
    private static final Instant NOW = Instant.now();

    private final PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final PlayerWriteRepository mockedPlayerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    private final PlayerTrainingBloomEventWriteRepository mockedPlayerTrainingBloomEventWriteRepository = Mockito.mock(
        PlayerTrainingBloomEventWriteRepository.class);

    private final GenerateBloomPhaseUseCase generateBloomPhaseUseCase = new GenerateBloomPhaseUseCase(mockedPlayerReadRepository,
        mockedPlayerWriteRepository, mockedPlayerTrainingBloomEventWriteRepository);

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
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            generateBloomPhaseUseCase.generate(bloomYears, bloomSpeed, bloomStart, playerId));
        assertEquals("Player not found.", exception.getMessage());
    }

    @Test
    @DisplayName("Generate should call generateAndStoreEventOfBloomPhase and save to repository.")
    void generate_should_call_generateAndStoreEventOfBloomPhase_and_save_to_repository() {

        Player playerMock = mock(Player.class);
        PlayerTrainingBloomEvent playerTrainingBloomEventMock = mock(PlayerTrainingBloomEvent.class);

        when(mockedPlayerReadRepository.findOneById(PLAYER_ID)).thenReturn(Optional.of(playerMock));
        when(mockedPlayerTrainingBloomEventWriteRepository.save(any())).thenReturn(playerTrainingBloomEventMock);

        try (
            MockedStatic<EventId> eventIdMockedStatic = Mockito.mockStatic(EventId.class);
            MockedStatic<InstantProvider> instantMockedStatic = Mockito.mockStatic(InstantProvider.class);
        ) {
            eventIdMockedStatic.when(EventId::generate).thenReturn(EVENT_ID);
            instantMockedStatic.when(InstantProvider::now).thenReturn(NOW);

            // Act
            generateBloomPhaseUseCase.generate(BLOOM_YEARS_ON, BLOOM_SPEED, BLOOM_START_AGE, PLAYER_ID);

            eventIdMockedStatic.verify(EventId::generate);
            eventIdMockedStatic.verifyNoMoreInteractions();
            instantMockedStatic.verify(InstantProvider::now);
            instantMockedStatic.verifyNoMoreInteractions();
        }

        // Assert
        ArgumentCaptor<PlayerTrainingBloomEvent> argumentCaptor = ArgumentCaptor.forClass(PlayerTrainingBloomEvent.class);
        verify(mockedPlayerTrainingBloomEventWriteRepository).save(argumentCaptor.capture());

        PlayerTrainingBloomEvent playerTrainingBloomEvent = argumentCaptor.getValue();

        assertEquals(PLAYER_ID, playerTrainingBloomEvent.getPlayerId());
        assertEquals(BLOOM_YEARS_ON, playerTrainingBloomEvent.getYearsOn());
        assertEquals(BLOOM_START_AGE, playerTrainingBloomEvent.getBloomStartAge());
        assertEquals(BLOOM_SPEED, playerTrainingBloomEvent.getBloomSpeed());
        assertEquals(EVENT_ID, playerTrainingBloomEvent.getId());
        assertEquals(NOW, playerTrainingBloomEvent.getOccurredAt());

        verify(playerMock).addBloomPhase(playerTrainingBloomEventMock);
        verify(mockedPlayerReadRepository).findOneById(PLAYER_ID);
        verify(mockedPlayerWriteRepository).save(playerMock);
        verifyNoMoreInteractions(playerMock, mockedPlayerReadRepository, mockedPlayerTrainingBloomEventWriteRepository);
    }

}
