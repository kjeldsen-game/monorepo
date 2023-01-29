package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.EventId;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventWriteRepository;
import com.kjeldsen.player.engine.PointsGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class GenerateSingleTrainingUseCase {

    private static final int FIRST_DAY_OF_TRAINING = 0;
    private final PlayerTrainingEventWriteRepository playerTrainingEventWriteRepository;

    public void generate(PlayerId playerId, List<PlayerSkill> skills, Integer days) {
        log.info("Generating training");

        IntStream.range(FIRST_DAY_OF_TRAINING, days)
            .forEach(currentDay -> skills.forEach(skill -> generateAndStoreEvent(playerId, skill, currentDay)));
    }

    private void generateAndStoreEvent(PlayerId playerId, PlayerSkill playerSkill, int currentDay) {
        PlayerTrainingEvent playerTrainingEvent = PlayerTrainingEvent.builder()
            .eventId(EventId.generate())
            .eventDate(Instant.now())
            .playerId(playerId)
            .skill(playerSkill)
            .points(PointsGenerator.generateRise(currentDay))
            .build();
        playerTrainingEventWriteRepository.save(playerTrainingEvent);
    }

}
