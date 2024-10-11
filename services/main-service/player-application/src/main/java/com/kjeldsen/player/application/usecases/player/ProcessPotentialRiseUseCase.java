package com.kjeldsen.player.application.usecases.player;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import com.kjeldsen.player.domain.generator.PotentialRiseGenerator;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerPotentialRiseEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessPotentialRiseUseCase {

    private static final Integer MAX_AGE = 21;
    private final PlayerPotentialRiseEventWriteRepository playerPotentialRiseEventWriteRepository;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;

    /* HOW TO HANDLE PROCESS POTENTIAL RISE
     * 1. Get all players which are under 21
     * 2. Get Rise + RandomSkill
     * 3. Check if the Rise happened if happened update potential + save event
     *
     *
     * NOTE!! No need to use the current day here etc. as the day is not factor in this case
     * as PlayerTrainingUseCase, Rise is based only on PotentialRiseGenerator w some % probability
     */

    public void process() {
        List<Player> players = playerReadRepository.findPlayerUnderAge(MAX_AGE);
        log.info("Running PotentialRiseUseCase for {} players", players.size());
        players.forEach(
            player -> {
                if (player.getAge().getYears() >= MAX_AGE) {
                    throw new IllegalArgumentException("The age of the player must be less than 21 years.");
                }
                Integer rise = PotentialRiseGenerator.generatePotentialRaise();
                PlayerSkill randomSkill = PlayerProvider.randomSkillForSpecificPlayer(player);
                if (rise != 0) { // Rise happened
                    log.info("Rise happened for player {} w points {} skill {}", player.getName(), rise,randomSkill);
                    generateEventAndUpdatePlayerPotential(player, randomSkill, rise);
                }
            }
        );
    }

    private void generateEventAndUpdatePlayerPotential(Player player, PlayerSkill randomSkill,Integer rise) {
        PlayerPotentialRiseEvent playerPotentialRiseEvent = PlayerPotentialRiseEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(player.getId())
            .potentialBeforeRaise(player.getPotentialSkillPoints(randomSkill))
            .pointsToRise(rise)
            .actualPoints(player.getActualSkillPoints(randomSkill))
            .skillThatRisen(randomSkill)
            .build();

        player.addSkillsPotentialRisePoints(randomSkill, rise);

        playerPotentialRiseEvent.setPotentialAfterRaise(player.getPotentialSkillPoints(randomSkill));

        playerPotentialRiseEventWriteRepository.save(playerPotentialRiseEvent);
        playerWriteRepository.save(player);
    }
}