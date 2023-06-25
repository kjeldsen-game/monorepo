package com.kjeldsen.player.application.usecases;

import com.kjeldsen.events.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
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

class GenerateDeclinePhaseUseCaseTest {

    private static final Integer DECLINE_START_AGE = 17;
    private static final Integer DECLINE_SPEED = 56;
    private static final Player.PlayerId PLAYER_ID = mock(Player.PlayerId.class);
    private static final EventId EVENT_ID = EventId.from("event-id");
    private static final Instant NOW = Instant.now();

    private final PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final PlayerWriteRepository mockedPlayerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    private final PlayerTrainingDeclineEventWriteRepository mockedPlayerTrainingDeclineEventWriteRepository = Mockito.mock(PlayerTrainingDeclineEventWriteRepository.class);

    private final GenerateSingleDeclineTrainingUseCase generateDeclinePhaseUseCase = new GenerateSingleDeclineTrainingUseCase(mockedPlayerTrainingDeclineEventWriteRepository, mockedPlayerReadRepository, mockedPlayerWriteRepository);

    @Test
    @DisplayName("Generate should throw if player not found.")
    void generate_should_throw_exception_if_player_not_found() {
        // Arrange
        Player.PlayerId playerId = new Player.PlayerId("test");

        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> generateDeclinePhaseUseCase.generate(PLAYER_ID, DECLINE_SPEED, DECLINE_START_AGE));
    }

    @Test
    @DisplayName("Generate should call generateAndStoreEventOfDeclinePhase and save to repository.")
    void generate_should_call_generateAndStoreEventOfDeclinePhase_and_save_to_repository() {

        //Arrange
        Player playerMock = mock(Player.class);
        PlayerTrainingDeclineEvent playerTrainingDeclineEventMock = mock(PlayerTrainingDeclineEvent.class);

        when(mockedPlayerReadRepository.findOneById(PLAYER_ID)).thenReturn(Optional.of(playerMock));
        when(mockedPlayerTrainingDeclineEventWriteRepository.save(any())).thenReturn(playerTrainingDeclineEventMock);

        try (
            MockedStatic<EventId> eventIdMockedStatic = Mockito.mockStatic(EventId.class);
            MockedStatic<InstantProvider> instantMockedStatic = Mockito.mockStatic(InstantProvider.class);
        ) {
            eventIdMockedStatic.when(EventId::generate).thenReturn(EVENT_ID);
            instantMockedStatic.when(InstantProvider::now).thenReturn(NOW);

            // Act
            generateDeclinePhaseUseCase.generate(PLAYER_ID, DECLINE_START_AGE, DECLINE_SPEED);

            eventIdMockedStatic.verify(EventId::generate);
            eventIdMockedStatic.verifyNoMoreInteractions();
            instantMockedStatic.verify(InstantProvider::now);
            instantMockedStatic.verifyNoMoreInteractions();
        }

        // Assert
        ArgumentCaptor<PlayerTrainingDeclineEvent> argumentCaptor = ArgumentCaptor.forClass(PlayerTrainingDeclineEvent.class);
        verify(mockedPlayerTrainingDeclineEventWriteRepository)
            .save(argumentCaptor.capture());

        PlayerTrainingDeclineEvent playerTrainingDeclineEvent = argumentCaptor.getValue();

        assertEquals(EVENT_ID, playerTrainingDeclineEvent.getId());
        assertEquals(NOW, playerTrainingDeclineEvent.getOccurredAt());

        verify(mockedPlayerReadRepository).findOneById(PLAYER_ID);
        verify(mockedPlayerTrainingDeclineEventWriteRepository).save(playerTrainingDeclineEvent);
        verifyNoMoreInteractions(playerMock, mockedPlayerReadRepository, mockedPlayerTrainingDeclineEventWriteRepository);

        assertEquals(PLAYER_ID, playerTrainingDeclineEvent.getPlayerId());
        assertEquals(DECLINE_START_AGE, playerTrainingDeclineEvent.getDeclineStartAge());
        assertEquals(DECLINE_SPEED, playerTrainingDeclineEvent.getDeclineSpeed());
        verify(mockedPlayerReadRepository).findOneById(PLAYER_ID);
        verify(mockedPlayerTrainingDeclineEventWriteRepository).save(any());
    }
}
