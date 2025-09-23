package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.application.usecases.trainings.GetActiveScheduledTrainingsUseCase;
import com.kjeldsen.player.application.usecases.trainings.player.SchedulePlayerTrainingUseCase;
import com.kjeldsen.player.application.usecases.trainings.GetTrainingEventsUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;

import com.kjeldsen.player.rest.api.TrainingApiDelegate;
import com.kjeldsen.player.rest.mapper.PlayerMapper;
import com.kjeldsen.player.rest.mapper.TrainingEventMapper;
import com.kjeldsen.player.rest.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@Component
public class TrainingDelegate implements TrainingApiDelegate {

    private final GetTeamUseCase getTeamUseCase;
    private final PlayerReadRepository playerReadRepository;

    private final SchedulePlayerTrainingUseCase schedulePlayerTrainingUseCase;
    private final GetActiveScheduledTrainingsUseCase getActiveScheduledTrainingsUseCase;
    private final GetTrainingEventsUseCase getTrainingEventsUseCase;

    @Override
    public ResponseEntity<SuccessResponse> schedulePlayerTraining(String playerId, SchedulePlayerTrainingRequest schedulePlayerTrainingRequest) {
        // Access denied as the player is not in your Team
        Optional<Player> optionalPlayer = playerReadRepository.findOneById(Player.PlayerId.of(playerId));
        if (optionalPlayer.isPresent() && !optionalPlayer.get().getTeamId()
            .equals(getTeamUseCase.get(SecurityUtils.getCurrentUserId()).getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        com.kjeldsen.player.domain.PlayerSkill skill = PlayerMapper.INSTANCE.map(schedulePlayerTrainingRequest.getSkill().getValue());
        schedulePlayerTrainingUseCase.schedule(Player.PlayerId.of(playerId), skill);
        return ResponseEntity.ok(new SuccessResponse().message("Training was successfully scheduled!"));
    }

    @Override
    public ResponseEntity<List<PlayerScheduledTrainingResponse>> getScheduledPlayerTrainings(String teamId, PlayerPosition position) {
        // Access denied as the path teamId is different that the teamId from Token
        if (!Objects.equals(getTeamUseCase.get(SecurityUtils.getCurrentUserId()).getId().value(), teamId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<GetActiveScheduledTrainingsUseCase.PlayerScheduledTraining> playerScheduled =
            getActiveScheduledTrainingsUseCase.get(Team.TeamId.of(teamId), PlayerMapper.INSTANCE.playerPositionMap(position));
        return ResponseEntity.ok(TrainingEventMapper.INSTANCE.mapPlayerScheduledTrainingList(playerScheduled));
    }

    @Override
    public ResponseEntity<TeamTrainingEventsResponse> getTeamTrainingEvents(String teamId, TrainingType trainingType, PlayerPosition position) {
        Map<String, List<TrainingEventResponse>> response = TrainingEventMapper.INSTANCE.fromTrainingEventsMap(
            getTrainingEventsUseCase.get(teamId, TrainingEventMapper.INSTANCE.fromTrainingType(trainingType),
                null,PlayerMapper.INSTANCE.playerPositionMap(position)));
        return ResponseEntity.ok(new TeamTrainingEventsResponse().trainings(response));
    }
}
