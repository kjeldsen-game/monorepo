package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.EventId;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventWriteRepository;
import com.kjeldsen.player.engine.PointsGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenerateSingleTrainingUseCaseTest {

    final private PlayerTrainingEventWriteRepository playerTrainingEventWriteRepository = Mockito.mock(PlayerTrainingEventWriteRepository.class);
    final private GenerateSingleTrainingUseCase generateSingleTrainingUseCase = new GenerateSingleTrainingUseCase(playerTrainingEventWriteRepository);

    @Test
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
            pointsGeneratorMockedStatic.when(() -> PointsGenerator.generateRise(0)).thenReturn(5);
            pointsGeneratorMockedStatic.when(() -> PointsGenerator.generateRise(1)).thenReturn(3);

            generateSingleTrainingUseCase.generate(playerId, skills, days);

            eventIdMockedStatic.verify(EventId::generate, Mockito.times(4));
            instantMockedStatic.verify(Instant::now, Mockito.times(4));
            eventIdMockedStatic.verifyNoMoreInteractions();
            instantMockedStatic.verifyNoMoreInteractions();

            ArgumentCaptor<PlayerTrainingEvent> argumentCaptor = ArgumentCaptor.forClass(PlayerTrainingEvent.class);
            Mockito.verify(playerTrainingEventWriteRepository, Mockito.times(4)).save(argumentCaptor.capture());

            List<PlayerTrainingEvent> playerTrainingEvents = argumentCaptor.getAllValues();

            assertEquals(PlayerSkill.SCORE, playerTrainingEvents.get(0).getSkill());
            assertEquals(5, playerTrainingEvents.get(0).getPoints());
            assertEquals(PlayerSkill.CO, playerTrainingEvents.get(1).getSkill());
            assertEquals(5, playerTrainingEvents.get(1).getPoints());
            assertEquals(PlayerSkill.SCORE, playerTrainingEvents.get(2).getSkill());
            assertEquals(3, playerTrainingEvents.get(2).getPoints());
            assertEquals(PlayerSkill.CO, playerTrainingEvents.get(3).getSkill());
            assertEquals(3, playerTrainingEvents.get(3).getPoints());

            assertThat(playerTrainingEvents).allMatch(player -> player.getPlayerId().equals(playerId));

            ArgumentCaptor<PlayerTrainingEvent> argumentCaptor2 = ArgumentCaptor.forClass(PlayerTrainingEvent.class);
            Mockito.verify(playerTrainingEventWriteRepository, Mockito.times(4)).save(argumentCaptor2.capture());

            Mockito.verifyNoMoreInteractions(playerTrainingEventWriteRepository);
        }
    }
}
