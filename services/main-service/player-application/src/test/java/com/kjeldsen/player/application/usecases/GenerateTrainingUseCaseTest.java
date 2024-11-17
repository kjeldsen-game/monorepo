//package com.kjeldsen.player.application.usecases;
//
//import com.kjeldsen.domain.EventId;
//import com.kjeldsen.player.domain.Player;
//import com.kjeldsen.player.domain.PlayerAge;
//import com.kjeldsen.player.domain.PlayerPosition;
//import com.kjeldsen.player.domain.PlayerSkill;
//import com.kjeldsen.player.domain.PlayerSkillRelevance;
//import com.kjeldsen.player.domain.PlayerSkills;
//import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
//import com.kjeldsen.player.domain.generator.PointsGenerator;
//import com.kjeldsen.player.domain.provider.InstantProvider;
//import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
//import com.kjeldsen.player.domain.repositories.PlayerTrainingEventWriteRepository;
//import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.Instant;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@Disabled
//@ExtendWith(MockitoExtension.class)
//class GenerateTrainingUseCaseTest {
//
//    @Mock
//    private PlayerTrainingEventWriteRepository playerTrainingEventWriteRepository;
//    @Mock
//    private PlayerReadRepository playerReadRepository;
//    @Mock
//    private PlayerWriteRepository playerWriteRepository;
//    @InjectMocks
//    private GenerateTrainingUseCase generateTrainingUseCase;
//
//    @Test
//    @DisplayName("create an event where generate a training")
//    void generate() {
//
//        EventId eventId1 = EventId.generate();
//        EventId eventId2 = EventId.generate();
//        Instant now1 = Instant.now();
//        Instant now2 = Instant.now();
//
//        Player.PlayerId playerId = Player.PlayerId.generate();
//
//        when(playerReadRepository.findOneById(playerId)).thenReturn(Optional.of(getPlayer(playerId)));
//
//        Integer days = 1;
//
//        try (
//            MockedStatic<EventId> eventIdMockedStatic = Mockito.mockStatic(EventId.class);
//            MockedStatic<InstantProvider> instantMockedStatic = Mockito.mockStatic(InstantProvider.class);
//            MockedStatic<PointsGenerator> pointsGeneratorMockedStatic = Mockito.mockStatic(PointsGenerator.class)
//        ) {
//            eventIdMockedStatic.when(EventId::generate).thenReturn(eventId1).thenReturn(eventId2);
//            instantMockedStatic.when(InstantProvider::now).thenReturn(now1).thenReturn(now2);
//            pointsGeneratorMockedStatic.when(() -> PointsGenerator.generatePointsRise(0)).thenReturn(5);
//            pointsGeneratorMockedStatic.when(() -> PointsGenerator.generatePointsRise(1)).thenReturn(3);
//
//            generateTrainingUseCase.generate(playerId, PlayerSkill.SCORING, days);
//
//            eventIdMockedStatic.verify(EventId::generate);
//            instantMockedStatic.verify(InstantProvider::now);
//            eventIdMockedStatic.verifyNoMoreInteractions();
//            instantMockedStatic.verifyNoMoreInteractions();
//
//            ArgumentCaptor<PlayerTrainingEvent> argumentCaptor = ArgumentCaptor.forClass(PlayerTrainingEvent.class);
//            verify(playerTrainingEventWriteRepository).save(argumentCaptor.capture());
//
//            List<PlayerTrainingEvent> playerTrainingEvents = argumentCaptor.getAllValues();
//
//            assertEquals(1, playerTrainingEvents.size());
//
//            assertEquals(PlayerSkill.SCORING, playerTrainingEvents.get(0).getSkill());
//            assertEquals(8, playerTrainingEvents.get(0).getPoints());
//            assertEquals(13, playerTrainingEvents.get(0).getPointsAfterTraining());
//
//            assertThat(playerTrainingEvents).allMatch(player -> player.getPlayerId().equals(playerId));
//
//            verify(playerWriteRepository).save(any());
//            Mockito.verifyNoMoreInteractions(playerTrainingEventWriteRepository, playerWriteRepository);
//        }
//    }
//
//    @Test
//    @DisplayName("introduce 0 days of training")
//    void generate_training_where_days_is_zero_throw_exception() {
//
//        // Arrange
//        Integer days = 0;
//
//        // Act & Asserts
//        Assertions.assertThatThrownBy(() -> generateTrainingUseCase.validateDays(days)).isInstanceOf(IllegalArgumentException.class).hasMessage(
//            "Days must be between 1 and 1000");
//    }
//
//    private Player getPlayer(Player.PlayerId playerId) {
//        return Player.builder()
//            .position(PlayerPosition.FORWARD)
//            .age(PlayerAge.generateAgeOfAPlayer())
//            .id(playerId)
//            .actualSkills(new HashMap<>(Map.of(
//                PlayerSkill.SCORING, new PlayerSkills(5, 0, PlayerSkillRelevance.CORE),
//                PlayerSkill.CONSTITUTION, new PlayerSkills(3, 0, PlayerSkillRelevance.SECONDARY))))
//            .build();
//    }
//}
