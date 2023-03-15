package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.EventId;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GenerateBloomPhaseUseCase {
    private final PlayerReadRepository playerReadRepository;
    private final PlayerTrainingEventWriteRepository playerTrainingEventWriteRepository;

    public void generate(int bloomYears, int bloomSpeed, int bloomStart, Player player) {
        log.info("Generating bloom phase");
        generateAndStoreEventOfBloomPhase(bloomYears, bloomSpeed, bloomStart, player);
    }

    private void generateAndStoreEventOfBloomPhase(int bloomYears, int bloomSpeed, int bloomStart, Player player) {
        PlayerTrainingEvent playerTrainingEvent = PlayerTrainingEvent.builder()
            .eventId(EventId.generate())
            .eventDate(InstantProvider.now())
            .playerId(player.getId())
            .bloom()
            .decline()
            .build();
        playerTrainingEventWriteRepository.save(playerTrainingEvent);
    }


}
