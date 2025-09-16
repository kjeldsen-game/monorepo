package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.models.training.TrainingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* Use case for retrieving and grouping {@link TrainingEvent} for specific team,
* optionally filtered by type or occurrence. If the type is not specified all types are retrieved.
* Results are grouped by the occurrence date and sorted within each group by the {@link com.kjeldsen.player.domain.models.training.TrainingEvent.TrainingType}
* */
@Slf4j
@Component
@RequiredArgsConstructor
public class GetTrainingEventsUseCase extends BaseTrainingUseCase {

    public Map<String, List<TrainingEvent>> get(
        String teamId, TrainingEvent.TrainingType type, Instant time) {
        log.info("GetTrainingEventsUseCase for team={} type={} time={}", teamId, type , time);

        List<TrainingEvent> events = trainingEventReadRepository
            .findAllSuccessfulByTeamIdTypeOccurredAt(Team.TeamId.of(teamId), type, time);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return events.stream()
            .collect(Collectors.groupingBy(trainingEvent ->
                trainingEvent.getOccurredAt()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(formatter),
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    list -> list.stream()
                        .sorted(Comparator.comparingInt(event -> {
                            return switch (event.getType()) {
                                case PLAYER_TRAINING -> 1;
                                case DECLINE_TRAINING -> 2;
                                case POTENTIAL_RISE -> 3;
                            };
                        }))
                        .toList()
                )
            ));
    }
}
