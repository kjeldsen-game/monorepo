package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.FindAndProcessScheduledTrainingUseCase;
import com.kjeldsen.player.application.usecases.GenerateSingleDeclineTrainingUseCase;
import com.kjeldsen.player.application.usecases.ScheduleTrainingUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.rest.api.SimulatorApiDelegate;
import com.kjeldsen.player.rest.mapper.PlayerDeclineResponseMapper;
import com.kjeldsen.player.rest.mapper.PlayerMapper;
import com.kjeldsen.player.rest.mapper.PlayerTrainingResponseMapper;
import com.kjeldsen.player.rest.model.PlayerDeclineResponse;
import com.kjeldsen.player.rest.model.PlayerHistoricalTrainingResponse;
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
            .forEach(skillsToTrain -> scheduleTrainingUseCase.generate(
                Player.PlayerId.of(playerId),
                PlayerMapper.INSTANCE.map(skillsToTrain.getValue()),
                registerSimulatedScheduledTrainingRequest.getDays()
            ));

        List<PlayerTrainingEvent> trainings = findAndProcessScheduledTrainingUseCase.findAndProcess(InstantProvider.nowAsLocalDate())
            .stream()
            .filter(playerTrainingEvent -> playerTrainingEvent.getPlayerId().equals(Player.PlayerId.of(playerId)))
            .toList();

        return ResponseEntity.ok(new PlayerHistoricalTrainingResponse()
            .playerId(playerId)
            .trainings(trainings.stream()
                .map(PlayerTrainingResponseMapper.INSTANCE::fromPlayerTrainingEvent)
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

                declineEvents.add(PlayerDeclineResponseMapper.INSTANCE.map(declineEvent));

                if (declineEvent.getPointsToSubtract() > 0) {
                    currentDayForDecline.set(1);
                }
            });

        return ResponseEntity.ok(declineEvents);
    }
}
