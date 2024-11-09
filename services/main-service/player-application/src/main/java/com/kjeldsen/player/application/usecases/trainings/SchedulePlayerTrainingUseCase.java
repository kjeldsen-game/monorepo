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

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulePlayerTrainingUseCase {

    private final PlayerTrainingScheduledEventWriteRepository playerTrainingScheduledEventWriteRepository;
    private final PlayerTrainingScheduledEventReadRepository playerTrainingScheduledEventReadRepository;
    private final PlayerReadRepository playerReadRepository;

    public void schedule(Player.PlayerId playerId, List<PlayerSkill> skills) {
        log.info("ScheduleTrainingUseCase for {} skill {}", playerId, skills);

        // Check if player exists
        Player player = playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));

        // Get his active scheduled training if any exists
        List<PlayerTrainingScheduledEvent> activeTrainings = playerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings()
            .stream().filter(event -> event.getPlayerId().equals(playerId)).toList();

        // Filter missing skills
        List<PlayerSkill> missingSkills = skills.stream()
            .filter(skill -> activeTrainings.stream().noneMatch(event -> event.getSkill().equals(skill)))
            .toList();

        // Filter events that need to be set to INACTIVE
        List<PlayerTrainingScheduledEvent> setToInactiveEvents = activeTrainings.stream()
            .filter(event -> !skills.contains(event.getSkill()))
            .toList();

        // Schedule new Trainings and set old to INACTIVE
        setScheduledTrainingsInactive(setToInactiveEvents);
        missingSkills.forEach(playerSkill -> scheduleAndStoreEvent(player, playerSkill));
    }

    private void setScheduledTrainingsInactive(List<PlayerTrainingScheduledEvent> trainings) {
        for (PlayerTrainingScheduledEvent event : trainings) {
            event.setStatus(PlayerTrainingScheduledEvent.Status.INACTIVE);
            playerTrainingScheduledEventWriteRepository.save(event);
        }
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
