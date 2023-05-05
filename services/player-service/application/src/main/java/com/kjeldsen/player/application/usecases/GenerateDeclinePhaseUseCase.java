package com.kjeldsen.player.application.usecases;

import com.kjeldsen.events.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GenerateDeclinePhaseUseCase {
    private final PlayerReadRepository playerReadRepository;
    private final PlayerTrainingDeclineEventWriteRepository playerTrainingDeclineEventWriteRepository;

    public void generate(int declineSpeed, int declineStart, Player.PlayerId playerId) {
        log.info("Generating decline phase");
        playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));

        generateAndStoreEventOfDeclinePhase(declineSpeed, declineStart, playerId);
    }

    private void generateAndStoreEventOfDeclinePhase(int declineSpeed, int declineStart, Player.PlayerId playerId) {
        PlayerTrainingDeclineEvent playerTrainingDeclineEvent = PlayerTrainingDeclineEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(playerId)
            .declineSpeed(declineSpeed)
            .declineStartAge(declineStart)
            .build();
        playerTrainingDeclineEventWriteRepository.save(playerTrainingDeclineEvent);
    }

}
