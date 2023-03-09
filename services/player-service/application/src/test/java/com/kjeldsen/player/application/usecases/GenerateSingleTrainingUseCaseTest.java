package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.EventId;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventWriteRepository;
import com.kjeldsen.player.engine.PointsGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class GenerateSingleTrainingUseCaseTest {

    final private PlayerTrainingEventWriteRepository playerTrainingEventWriteRepository = Mockito.mock(PlayerTrainingEventWriteRepository.class);
    final private GenerateSingleTrainingUseCase generateSingleTrainingUseCase = new GenerateSingleTrainingUseCase(playerTrainingEventWriteRepository);

    @Test
    @DisplayName("create a event where generate a training")
    void generate() {

        EventId eventId1 = EventId.generate();
        EventId eventId2 = EventId.generate();
        Instant now1 = Instant.now();
        Instant now2 = Instant.now();

        PlayerId playerId = PlayerId.generate();
        List<PlayerSkill> skills = List.of(PlayerSkill.SCORE, PlayerSkill.CO);
        Integer days = 2;

        try (
            MockedStatic<EventId> eventIdMockedStatic = Mockito.mockStatic(EventId.class);
            MockedStatic<Instant> instantMockedStatic = Mockito.mockStatic(Instant.class);
            MockedStatic<PointsGenerator> pointsGeneratorMockedStatic = Mockito.mockStatic(PointsGenerator.class);
        ) {
            eventIdMockedStatic.when(EventId::generate).thenReturn(eventId1).thenReturn(eventId2);
            instantMockedStatic.when(Instant::now).thenReturn(now1).thenReturn(now2);
            pointsGeneratorMockedStatic.when(() -> PointsGenerator.generatePointsRise(0)).thenReturn(5);
            pointsGeneratorMockedStatic.when(() -> PointsGenerator.generatePointsRise(1)).thenReturn(3);

            generateSingleTrainingUseCase.generate(playerId, skills, days);

            eventIdMockedStatic.verify(EventId::generate, times(4));
            instantMockedStatic.verify(Instant::now, times(4));
            eventIdMockedStatic.verifyNoMoreInteractions();
            instantMockedStatic.verifyNoMoreInteractions();

            ArgumentCaptor<PlayerTrainingEvent> argumentCaptor = ArgumentCaptor.forClass(PlayerTrainingEvent.class);
            verify(playerTrainingEventWriteRepository, times(4)).save(argumentCaptor.capture());

            List<PlayerTrainingEvent> playerTrainingEvents = argumentCaptor.getAllValues();

            assertEquals(4, playerTrainingEvents.size());

            assertEquals(PlayerSkill.SCORE, playerTrainingEvents.get(0).getSkill());
            assertEquals(5, playerTrainingEvents.get(0).getPoints());
            assertEquals(PlayerSkill.CO, playerTrainingEvents.get(1).getSkill());
            assertEquals(5, playerTrainingEvents.get(1).getPoints());
            assertEquals(PlayerSkill.SCORE, playerTrainingEvents.get(2).getSkill());
            assertEquals(3, playerTrainingEvents.get(2).getPoints());
            assertEquals(PlayerSkill.CO, playerTrainingEvents.get(3).getSkill());
            assertEquals(3, playerTrainingEvents.get(3).getPoints());

            assertThat(playerTrainingEvents).allMatch(player -> player.getPlayerId().equals(playerId));

            Mockito.verifyNoMoreInteractions(playerTrainingEventWriteRepository);
        }
    }

    @Test
    @DisplayName("introduce a list of skills that is empty")
    public void generate_training_where_skills_is_empty_throw_exception() {

        // Arrange
        List<PlayerSkill> skills = List.of();

        // Act & Asserts
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> generateSingleTrainingUseCase.checkNotNullOrEmpty(skills)).isInstanceOf(IllegalArgumentException.class).hasMessage("Skills cannot be null or empty");
    }


    @Test
    @DisplayName("introduce 0 days of training")
    public void generate_training_where_days_is_zero_throw_exception() {

        // Arrange
        Integer days = 0;

        // Act & Asserts
        Assertions.assertThatThrownBy(() -> generateSingleTrainingUseCase.checkIfDaysIsValid(days)).isInstanceOf(IllegalArgumentException.class).hasMessage("Days must be between 1 and 1000");
    }
}
