package com.kjeldsen.player.application.usecases;

import com.kjeldsen.events.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingBloomEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GenerateBloomPhaseUseCase {

    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;
    private final PlayerTrainingBloomEventWriteRepository playerTrainingBloomEventWriteRepository;

    public void generate(int bloomYears, int bloomSpeed, int bloomStart, Player.PlayerId playerId) {
        log.info("Generating bloom phase");
        Player player = playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));
        PlayerTrainingBloomEvent bloomEvent = generateAndStoreEventOfBloomPhase(bloomYears, bloomSpeed, bloomStart, playerId);
        player.addBloomPhase(bloomEvent);
        playerWriteRepository.save(player);
    }

    private PlayerTrainingBloomEvent generateAndStoreEventOfBloomPhase(int bloomYears, int bloomSpeed, int bloomStart, Player.PlayerId playerId) {
        PlayerTrainingBloomEvent playerTrainingBloomEvent = PlayerTrainingBloomEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(playerId)
            .yearsOn(bloomYears)
            .bloomSpeed(bloomSpeed)
            .bloomStartAge(bloomStart)
            .build();
        return playerTrainingBloomEventWriteRepository.save(playerTrainingBloomEvent);
    }
}
