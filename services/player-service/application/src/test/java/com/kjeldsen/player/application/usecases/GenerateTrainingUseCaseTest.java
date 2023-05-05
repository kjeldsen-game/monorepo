package com.kjeldsen.player.application.usecases;

import com.kjeldsen.events.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.generator.PointsGenerator;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingBloomEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GenerateTrainingUseCaseTest {

    final private PlayerTrainingEventWriteRepository playerTrainingEventWriteRepository = Mockito.mock(PlayerTrainingEventWriteRepository.class);
    final private PlayerReadRepository playerReadRepository = Mockito.mock(PlayerReadRepository.class);
    final private PlayerWriteRepository playerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    final private PlayerTrainingBloomEventReadRepository playerTrainingBloomEventReadRepository = Mockito.mock(
        PlayerTrainingBloomEventReadRepository.class);

    final private GenerateTrainingUseCase generateTrainingUseCase = new GenerateTrainingUseCase(playerTrainingEventWriteRepository,
        playerReadRepository, playerWriteRepository, playerTrainingBloomEventReadRepository);

    @Test
    @DisplayName("create a event where generate a training")
    void generate() {

        EventId eventId1 = EventId.generate();
        EventId eventId2 = EventId.generate();
        Instant now1 = Instant.now();
        Instant now2 = Instant.now();

        Player.PlayerId playerId = Player.PlayerId.generate();

        when(playerReadRepository.findOneById(playerId)).thenReturn(Optional.of(getPlayer(playerId)));

        Integer days = 1;

        try (
            MockedStatic<EventId> eventIdMockedStatic = Mockito.mockStatic(EventId.class);
            MockedStatic<Instant> instantMockedStatic = Mockito.mockStatic(Instant.class);
            MockedStatic<PointsGenerator> pointsGeneratorMockedStatic = Mockito.mockStatic(PointsGenerator.class)
        ) {
            eventIdMockedStatic.when(EventId::generate).thenReturn(eventId1).thenReturn(eventId2);
            instantMockedStatic.when(Instant::now).thenReturn(now1).thenReturn(now2);
            pointsGeneratorMockedStatic.when(() -> PointsGenerator.generatePointsRise(0)).thenReturn(5);
            pointsGeneratorMockedStatic.when(() -> PointsGenerator.generatePointsRise(1)).thenReturn(3);

            generateTrainingUseCase.generate(playerId, PlayerSkill.SCORE, days);

            eventIdMockedStatic.verify(EventId::generate);
            instantMockedStatic.verify(Instant::now);
            eventIdMockedStatic.verifyNoMoreInteractions();
            instantMockedStatic.verifyNoMoreInteractions();

            ArgumentCaptor<PlayerTrainingEvent> argumentCaptor = ArgumentCaptor.forClass(PlayerTrainingEvent.class);
            verify(playerTrainingEventWriteRepository).save(argumentCaptor.capture());

            List<PlayerTrainingEvent> playerTrainingEvents = argumentCaptor.getAllValues();

            assertEquals(1, playerTrainingEvents.size());

            assertEquals(PlayerSkill.SCORE, playerTrainingEvents.get(0).getSkill());
            assertEquals(3, playerTrainingEvents.get(0).getPoints());

            assertThat(playerTrainingEvents).allMatch(player -> player.getPlayerId().equals(playerId));

            Mockito.verifyNoMoreInteractions(playerTrainingEventWriteRepository);
        }
    }

    @Test
    @DisplayName("introduce 0 days of training")
    void generate_training_where_days_is_zero_throw_exception() {

        // Arrange
        Integer days = 0;

        // Act & Asserts
        Assertions.assertThatThrownBy(() -> generateTrainingUseCase.validateDays(days)).isInstanceOf(IllegalArgumentException.class).hasMessage(
            "Days must be between 1 and 1000");
    }

    private Player getPlayer(Player.PlayerId playerId) {
        return Player.builder()
            .id(playerId)
            .actualSkills(new HashMap<>(Map.of(
                PlayerSkill.SCORE, 5,
                PlayerSkill.CO, 3)))
            .build();
    }
}
