package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.FindAndProcessScheduledTrainingUseCase;
import com.kjeldsen.player.application.usecases.ScheduleTrainingUseCase;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.rest.api.SimulatorApiDelegate;
import com.kjeldsen.player.rest.model.PlayerHistoricalTrainingResponse;
import com.kjeldsen.player.rest.model.PlayerSkill;
import com.kjeldsen.player.rest.model.PlayerTrainingResponse;
import com.kjeldsen.player.rest.model.RegisterSimulatedScheduledTrainingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class SimulatorDelegate implements SimulatorApiDelegate {

    private final ScheduleTrainingUseCase scheduleTrainingUseCase;
    private final FindAndProcessScheduledTrainingUseCase findAndProcessScheduledTrainingUseCase;

    @Override
    public ResponseEntity<PlayerHistoricalTrainingResponse> registerSimulatedScheduledTraining(
        String playerId,
        RegisterSimulatedScheduledTrainingRequest registerSimulatedScheduledTrainingRequest) {

        scheduleTrainingUseCase.generate(
            PlayerId.of(playerId),
            registerSimulatedScheduledTrainingRequest.getSkills()
                .stream()
                .map(this::playerSkill2DomainPlayerSkill)
                .collect(Collectors.toSet()),
            registerSimulatedScheduledTrainingRequest.getDays()
        );

        List<PlayerTrainingEvent> trainings =  findAndProcessScheduledTrainingUseCase.findAndProcess(InstantProvider.nowAsLocalDate())
            .stream()
            .filter(playerTrainingEvent -> playerTrainingEvent.getPlayerId().equals(PlayerId.of(playerId)))
            .toList();

        return ResponseEntity.ok(new PlayerHistoricalTrainingResponse()
            .playerId(playerId)
            .trainings(trainings.stream()
                .map(this::playerTrainingEvent2PlayerTrainingResponse)
                .toList()));
    }

    private com.kjeldsen.player.domain.PlayerSkill playerSkill2DomainPlayerSkill(PlayerSkill playerSkill) {
        return com.kjeldsen.player.domain.PlayerSkill.valueOf(playerSkill.name());
    }

    private PlayerSkill playerSkill2DomainPlayerSkill(com.kjeldsen.player.domain.PlayerSkill playerSkill) {
        return PlayerSkill.valueOf(playerSkill.name());
    }

    private PlayerTrainingResponse playerTrainingEvent2PlayerTrainingResponse(PlayerTrainingEvent playerTrainingEvent) {
        return new PlayerTrainingResponse()
            .currentDay(playerTrainingEvent.getCurrentDay())
            .playerId(playerTrainingEvent.getPlayerId().toString())
            .skill(playerSkill2DomainPlayerSkill(playerTrainingEvent.getSkill()))
            .points(playerTrainingEvent.getPoints())
            .pointsBeforeTraining(playerTrainingEvent.getPointsBeforeTraining())
            .pointsAfterTraining(playerTrainingEvent.getPointsAfterTraining());
    }

}
