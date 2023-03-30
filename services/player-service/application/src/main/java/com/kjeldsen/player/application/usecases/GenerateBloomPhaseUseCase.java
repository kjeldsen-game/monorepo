package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingBloomEventWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GenerateBloomPhaseUseCase {
    private final PlayerReadRepository playerReadRepository;
    private final PlayerTrainingBloomEventWriteRepository playerTrainingBloomEventWriteRepository;

    public void generate(int bloomYears, int bloomSpeed, int bloomStart, PlayerId playerId) {
        log.info("Generating bloom phase");
        playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));

        generateAndStoreEventOfBloomPhase(bloomYears, bloomSpeed, bloomStart, playerId);
    }

    private void generateAndStoreEventOfBloomPhase(int bloomYears, int bloomSpeed, int bloomStart, PlayerId playerId) {
        PlayerTrainingBloomEvent playerTrainingBloomEvent = PlayerTrainingBloomEvent.builder()
            .playerId(playerId)
            .yearsOn(bloomYears)
            .bloomSpeed(bloomSpeed)
            .bloomStartAge(bloomStart)
            .build();
        playerTrainingBloomEventWriteRepository.save(playerTrainingBloomEvent);
    }

}
