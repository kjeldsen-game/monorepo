package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.player.application.testdata.TrainingTestData;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.models.training.TrainingEvent;
import com.kjeldsen.player.domain.repositories.training.TrainingEventReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
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

        when(trainingEventReadRepository.findAllSuccessfulByTeamIdTypeOccurredAt(Team.TeamId.of("teamId"),
            type, null)).thenReturn(events);

        Map<String, List<TrainingEvent>> result = getTrainingEventsUseCase.get(
            "teamId", type, null);
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should return all training events together")
    void should_return_all_training_events_together() {
        List<TrainingEvent> events = Stream.of(
            TrainingTestData.getTrainingEvents(null, TrainingEvent.TrainingType.DECLINE_TRAINING),
            TrainingTestData.getTrainingEvents(null, TrainingEvent.TrainingType.PLAYER_TRAINING),
            TrainingTestData.getTrainingEvents(null, TrainingEvent.TrainingType.POTENTIAL_RISE)
        ).flatMap(List::stream).toList();

        when(trainingEventReadRepository.findAllSuccessfulByTeamIdTypeOccurredAt(Team.TeamId.of("teamId"),
            null, null)).thenReturn(events);

        Map<String, List<TrainingEvent>> result = getTrainingEventsUseCase.get(
            "teamId", null, null);
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);

        assertThat(result.values().iterator().next()).hasSize(3);
    }
}