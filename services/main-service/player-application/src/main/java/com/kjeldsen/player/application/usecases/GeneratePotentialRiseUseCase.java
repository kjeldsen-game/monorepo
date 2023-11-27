package com.kjeldsen.player.application.usecases;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerPotentialRiseEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.kjeldsen.player.domain.generator.PotentialRiseGenerator.generateRaiseProbability;

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

        int rise = generateRaiseProbability();

        if (player.getAge() <= MAX_AGE && rise != 0) {
            return generateAndStoreEvent(player, rise);
        } else {
            return null;
        }
    }

    private PlayerPotentialRiseEvent generateAndStoreEvent(Player player, int rise) {

        PlayerPotentialRiseEvent playerPotentialRiseEvent = PlayerPotentialRiseEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(player.getId())
            .potentialBeforeRaise(0)
            .pointsToRise(rise)
            .potentialAfterRaise(0)
            .skillThatRisen(PlayerProvider.randomSkill())
            .build();

        playerPotentialRiseEvent = playerPotentialRiseEventWriteRepository.save(playerPotentialRiseEvent);
        playerWriteRepository.save(player);

        return playerPotentialRiseEvent;
    }
}
