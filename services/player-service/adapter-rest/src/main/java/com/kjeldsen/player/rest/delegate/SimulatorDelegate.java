package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.FindAndProcessScheduledTrainingUseCase;
import com.kjeldsen.player.application.usecases.GenerateSingleDeclineTrainingUseCase;
import com.kjeldsen.player.application.usecases.ScheduleTrainingUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.rest.api.SimulatorApiDelegate;
import com.kjeldsen.player.rest.model.PlayerDeclineResponse;
import com.kjeldsen.player.rest.model.PlayerHistoricalTrainingResponse;
import com.kjeldsen.player.rest.model.PlayerSkill;
import com.kjeldsen.player.rest.model.PlayerTrainingResponse;
import com.kjeldsen.player.rest.model.RegisterSimulatedDeclineRequest;
import com.kjeldsen.player.rest.model.RegisterSimulatedScheduledTrainingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class SimulatorDelegate implements SimulatorApiDelegate {

    private final ScheduleTrainingUseCase scheduleTrainingUseCase;
    private final FindAndProcessScheduledTrainingUseCase findAndProcessScheduledTrainingUseCase;
    private final GenerateSingleDeclineTrainingUseCase generateSingleDeclineTrainingUseCase;

    @Override
    public ResponseEntity<PlayerHistoricalTrainingResponse> registerSimulatedScheduledTraining(
        String playerId,
        RegisterSimulatedScheduledTrainingRequest registerSimulatedScheduledTrainingRequest) {

        registerSimulatedScheduledTrainingRequest.getSkills()
            .forEach(playerSkill -> scheduleTrainingUseCase.generate(
                Player.PlayerId.of(playerId),
                this.playerSkill2DomainPlayerSkill(playerSkill),
                registerSimulatedScheduledTrainingRequest.getDays()
            ));

        List<PlayerTrainingEvent> trainings = findAndProcessScheduledTrainingUseCase.findAndProcess(InstantProvider.nowAsLocalDate())
            .stream()
            .filter(playerTrainingEvent -> playerTrainingEvent.getPlayerId().equals(Player.PlayerId.of(playerId)))
            .toList();

        return ResponseEntity.ok(new PlayerHistoricalTrainingResponse()
            .playerId(playerId)
            .trainings(trainings.stream()
                .map(this::playerTrainingEvent2PlayerTrainingResponse)
                .toList()));
    }

    @Override
    public ResponseEntity<List<PlayerDeclineResponse>> registerSimulatedDecline(String playerId,
        RegisterSimulatedDeclineRequest registerSimulatedDeclineRequest) {

        List<PlayerDeclineResponse> declineEvents = new ArrayList<>();

        final AtomicInteger currentDayForDecline = new AtomicInteger(1);
        IntStream.rangeClosed(1, registerSimulatedDeclineRequest.getDaysToDecline())
            .forEach(i -> {
                PlayerTrainingDeclineEvent declineEvent = generateSingleDeclineTrainingUseCase.generate(
                    Player.PlayerId.of(playerId),
                    currentDayForDecline.getAndIncrement(),
                    registerSimulatedDeclineRequest.getDeclineSpeed());

                declineEvents.add(playerTrainingDeclineEventToPlayerDeclineResponse(declineEvent));

                if (declineEvent.getPointsToSubtract() > 0) {
                    currentDayForDecline.set(1);
                }
            });

        return ResponseEntity.ok(declineEvents);
    }

    private PlayerDeclineResponse playerTrainingDeclineEventToPlayerDeclineResponse(
        PlayerTrainingDeclineEvent playerTrainingDeclineEvent) {
        return new PlayerDeclineResponse()
            .currentDay(playerTrainingDeclineEvent.getCurrentDay())
            .playerId(playerTrainingDeclineEvent.getPlayerId().toString())
            .skill(playerSkill2DomainPlayerSkill(playerTrainingDeclineEvent.getSkill()))
            .pointsToSubtract(playerTrainingDeclineEvent.getPointsToSubtract())
            .pointsBeforeTraining(playerTrainingDeclineEvent.getPointsBeforeTraining())
            .pointsAfterTraining(playerTrainingDeclineEvent.getPointsAfterTraining());
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
