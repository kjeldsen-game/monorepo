package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulePlayerTrainingUseCase {

    private final PlayerTrainingScheduledEventWriteRepository playerTrainingScheduledEventWriteRepository;
    private final PlayerTrainingScheduledEventReadRepository playerTrainingScheduledEventReadRepository;
    private final PlayerReadRepository playerReadRepository;

    /*
    * SchedulePlayerTrainingUseCase is use case that is make by User/Team when he chose which player skill should
    * be scheduled for the training. Word "Schedule" indicate that this happened by User on request. By this we mean that
    * we store event but the handling of processing all training (all players) or specific training (one player) is handled
    * by different use case.
    *
    * The workflow of PlayerTraining: Schedule -> Process -> Execute
    */

    public void schedule(Player.PlayerId playerId, PlayerSkill skill) {
        log.info("ScheduleTrainingUseCase for {} skill {}", playerId, skill);

        // Check if player exists
        Player player = playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));

        // Get his active scheduled training if any exists
        List<PlayerTrainingScheduledEvent> activeTrainings = playerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings()
            .stream().filter(event -> event.getPlayerId().equals(playerId)).toList();

        // Check if the skill in the request is not already set up for the training, if yes throw error or don't modify
        if (activeTrainings.stream().anyMatch(sk -> sk.getSkill().equals(skill))) {
            throw new RuntimeException("Training for the skill is already scheduled.");
        }
        // Check the active trainings, if the size is less than 2, we could set it without removing active training
        if (activeTrainings.size() >= 2) {
            log.info("Removing scheduled training for older skill, because there were already 2 trainings assigned to player.");
            activeTrainings.stream()
                .min(Comparator.comparing(PlayerTrainingScheduledEvent::getOccurredAt))
                .ifPresent(this::setScheduledTrainingInactive);
        }
        scheduleAndStoreEvent(player, skill);
    }

    private void setScheduledTrainingInactive(PlayerTrainingScheduledEvent event) {
        event.setStatus(PlayerTrainingScheduledEvent.Status.INACTIVE);
        playerTrainingScheduledEventWriteRepository.save(event);
    }

    private void scheduleAndStoreEvent(Player player, PlayerSkill skill) {
        PlayerTrainingScheduledEvent playerTrainingScheduledEvent = PlayerTrainingScheduledEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(player.getId())
            .skill(skill)
            .status(PlayerTrainingScheduledEvent.Status.ACTIVE)
            .build();

        playerTrainingScheduledEventWriteRepository.save(playerTrainingScheduledEvent);
    }
}
