package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.player.application.testdata.TrainingTestData;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.models.training.TrainingEvent;
import com.kjeldsen.player.domain.repositories.training.TrainingEventReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.ARRAY;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTrainingEventsUseCaseTest {

    @Mock
    private  TrainingEventReadRepository trainingEventReadRepository;

    @InjectMocks
    private final GetTrainingEventsUseCase getTrainingEventsUseCase = new GetTrainingEventsUseCase();

    @ParameterizedTest
    @EnumSource(TrainingEvent.TrainingType.class)
    @DisplayName("Should return training event by type")
    void should_return_training_events(TrainingEvent.TrainingType type) {
        List<TrainingEvent> events = TrainingTestData.getTrainingEvents(null, type);

        when(trainingEventReadRepository.findAllSuccessfulByTeamIdTypePlayerPositionOccurredAt(Team.TeamId.of("teamId"),
            type, null, null)).thenReturn(events);

        Map<String, List<TrainingEvent>> result = getTrainingEventsUseCase.get(
            "teamId", type, null, null);
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
    }

    static Stream<Arguments> positionsExpectedSource() {
        return Stream.of(
            Arguments.of(PlayerPosition.GOALKEEPER, 1, TrainingTestData.getTrainingEvents(null, null)),
            Arguments.of(PlayerPosition.FORWARD, 0, List.of()),
            Arguments.of(PlayerPosition.DEFENSIVE_MIDFIELDER, 0, List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("positionsExpectedSource")
    @DisplayName("Should return trainings by position")
    void should_return_trainings_by_position(PlayerPosition position, int expected, List<TrainingEvent> events) {
        when(trainingEventReadRepository.findAllSuccessfulByTeamIdTypePlayerPositionOccurredAt(Team.TeamId.of("teamId"),
            null, null, position)).thenReturn(events);

        Map<String, List<TrainingEvent>> result = getTrainingEventsUseCase.get(
            "teamId", null, null, position);

        assertThat(result.size()).isEqualTo(expected);
    }


    @Test
    @DisplayName("Should return zero trainings by position and type")
    void should_return_trainings_by_position() {
        when(trainingEventReadRepository.findAllSuccessfulByTeamIdTypePlayerPositionOccurredAt(Team.TeamId.of("teamId"),
            TrainingEvent.TrainingType.POTENTIAL_RISE, null, PlayerPosition.FORWARD)).thenReturn(List.of());

        Map<String, List<TrainingEvent>> result = getTrainingEventsUseCase.get(
            "teamId", TrainingEvent.TrainingType.POTENTIAL_RISE, null, PlayerPosition.FORWARD);

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should return all training events together")
    void should_return_all_training_events_together() {
        List<TrainingEvent> events = Stream.of(
            TrainingTestData.getTrainingEvents(null, TrainingEvent.TrainingType.DECLINE_TRAINING),
            TrainingTestData.getTrainingEvents(null, TrainingEvent.TrainingType.PLAYER_TRAINING),
            TrainingTestData.getTrainingEvents(null, TrainingEvent.TrainingType.POTENTIAL_RISE)
        ).flatMap(List::stream).toList();

        when(trainingEventReadRepository.findAllSuccessfulByTeamIdTypePlayerPositionOccurredAt(Team.TeamId.of("teamId"),
            null, null, null)).thenReturn(events);

        Map<String, List<TrainingEvent>> result = getTrainingEventsUseCase.get(
            "teamId", null, null, null);
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);

        assertThat(result.values().iterator().next()).hasSize(3);
    }
}