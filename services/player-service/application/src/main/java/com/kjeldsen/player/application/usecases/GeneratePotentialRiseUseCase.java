package com.kjeldsen.player.application.usecases;

import com.kjeldsen.events.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.player.PlayerPotentialRiseEventWriteRepository;
import com.kjeldsen.player.domain.repositories.player.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.player.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GeneratePotentialRiseUseCase {
    private static final Integer MAX_AGE = 21;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;
    private final PlayerPotentialRiseEventWriteRepository playerPotentialRiseEventWriteRepository;

    public PlayerPotentialRiseEvent generate(Player.PlayerId playerId) {
        log.info("Generating potential rise");

        Player player = playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));

        if (player.getAge() <= MAX_AGE) {
            return generateAndStoreEvent(player);
        } else {
            return null;
        }
    }

    private PlayerPotentialRiseEvent generateAndStoreEvent(Player player) {

        PlayerPotentialRiseEvent playerPotentialRiseEvent = PlayerPotentialRiseEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(player.getId())
            .pointsBeforeRaise(0)
            .pointsAfterRaise(0)
            .skillThatRisen(PlayerProvider.randomSkill())
            .build();

        playerPotentialRiseEvent = playerPotentialRiseEventWriteRepository.save(playerPotentialRiseEvent);
        playerWriteRepository.save(player);

        return playerPotentialRiseEvent;
    }
}
