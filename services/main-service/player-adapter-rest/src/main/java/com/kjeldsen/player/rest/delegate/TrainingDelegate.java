package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.GetHistoricalTrainingUseCase;
import com.kjeldsen.player.application.usecases.trainings.SchedulePlayerTrainingUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.rest.api.TrainingApiDelegate;
import com.kjeldsen.player.rest.mapper.PlayerMapper;
import com.kjeldsen.player.rest.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TrainingDelegate implements TrainingApiDelegate {

    private final GetHistoricalTrainingUseCase getHistoricalTrainingUseCase;
    private final SchedulePlayerTrainingUseCase schedulePlayerTrainingUseCase;

    @Override
    public ResponseEntity<Void> schedulePlayerTraining(String playerId, SchedulePlayerTrainingRequest schedulePlayerTrainingRequest) {

        com.kjeldsen.player.domain.PlayerSkill skill = PlayerMapper.INSTANCE.map(schedulePlayerTrainingRequest.getSkill().getValue());
        schedulePlayerTrainingUseCase.schedule(Player.PlayerId.of(playerId), skill);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<PlayerHistoricalTrainingResponse> getHistoricalTraining(String playerId) {

        List<PlayerTrainingResponse> trainings = getHistoricalTrainingUseCase.get(Player.PlayerId.of(playerId))
            .stream()
            .map(this::playerTrainingEvent2PlayerTrainingResponse)
            .toList();

        PlayerHistoricalTrainingResponse playerHistoricalTrainingResponse = new PlayerHistoricalTrainingResponse()
            .playerId(playerId)
            .trainings(trainings);
        return ResponseEntity.ok(playerHistoricalTrainingResponse);
    }

    private PlayerSkill playerSkill2DomainPlayerSkill(com.kjeldsen.player.domain.PlayerSkill playerSkill) {
        return PlayerSkill.valueOf(playerSkill.name()); // TODO MUDO CAMBIA ESTO A UN MAPPER
    }

    private PlayerTrainingResponse playerTrainingEvent2PlayerTrainingResponse(PlayerTrainingEvent playerTrainingEvent) {
        return new PlayerTrainingResponse()
            .currentDay(playerTrainingEvent.getCurrentDay())
            .playerId(playerTrainingEvent.getPlayerId().toString())
            .skill(playerSkill2DomainPlayerSkill(playerTrainingEvent.getSkill()))
            .actualPoints(playerTrainingEvent.getActualPoints())
            .pointsBeforeTraining(playerTrainingEvent.getPointsBeforeTraining())
            .pointsAfterTraining(playerTrainingEvent.getPointsAfterTraining());
    }

}
