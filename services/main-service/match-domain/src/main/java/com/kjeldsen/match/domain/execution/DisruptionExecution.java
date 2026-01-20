package com.kjeldsen.match.domain.execution;

import com.kjeldsen.match.domain.entities.Play;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.duel.*;
import com.kjeldsen.match.domain.generator.RandomGenerator;
import com.kjeldsen.match.domain.recorder.GameProgressRecord;
import com.kjeldsen.match.domain.selection.ReceiverSelection;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;


/**
 * Determines the outcome of disruption execution that could happen in some duel types.
 */
@Slf4j
public class DisruptionExecution {

    private static final int MAX_TOTAL = 100;

    public static Optional<DuelDisruption> executeDisruption(DuelParams params, DuelDisruptor disruptor, GameState state, Integer total) {

        Optional<Play> lastPlay = state.lastPlay();

        return switch (disruptor) {
            case MISSED_PASS -> handleMissedPass(params, total, lastPlay.isPresent());
            case MISSED_SHOT -> handleMissedShot(total);
            case GOALKEEPER_FUMBLE -> handleGoalkeeperFumble(params);
            default -> Optional.empty();
        };
    }

    private static Optional<DuelDisruption> handleGoalkeeperFumble(DuelParams params) {

        Integer controlSkill = params.getChallenger().getSkillValue(PlayerSkill.CONTROL);
        Integer diceRoll = RandomGenerator.randomInt(0, 100);

        int controlResult = diceRoll - controlSkill;
        double controlSuccessChance = RandomGenerator.randomDouble(0.0, 1.0);

        if (controlSkill < diceRoll) {
            double threshold = 0.0;
            if (controlResult > 75) {
                threshold = 0.1;
            } else if (controlResult > 50) {
                threshold = 0.25;
            } else if (controlResult > 30) {
                threshold = 0.5;
            }

            if (threshold > 0.0 && controlSuccessChance < threshold) {
                return Optional.of(DuelDisruption.builder()
                    .type(DuelDisruptor.GOALKEEPER_FUMBLE)
                    .build());
            }
        }
        return Optional.empty();
    }

    private static Optional<DuelDisruption> handleMissedPass(DuelParams params, Integer total, boolean lastPlay ) {

        PitchArea candidatePitchArea;
        Player candidateChallenger = null; // TODO right now every pass duel is won or missed cannot lose yet
        Player candidateReceiver;

        if (total < MAX_TOTAL && lastPlay && !params.getDuelType().equals(DuelType.THROW_IN)) {
            double difference = (double) (MAX_TOTAL - total) / 2;
            int random = RandomGenerator.randomInt(0, 100);
            // If random is less than difference than pass receiver is changed
//            log.info("Total value = {} Difference value = {} and succeed = {}, missed is", total, difference, random);
            // Missed pass
            if (random < difference) {
                log.info("DisruptionExecution was successfull and the reciver of pass is gonna be changed");
                // Generate new pitch area, receiver and the challenger with adjusted values
                candidatePitchArea = params.getDestinationPitchArea().getNearbyAreas().get(RandomGenerator.randomInt(0, 1));

                candidateReceiver = ReceiverSelection.selectUnsuccessfulPassDuel(params, candidatePitchArea);

                DuelDisruption disruption = DuelDisruption.builder()
                    .type(DuelDisruptor.MISSED_PASS)
                    .random(random)
                    .difference(difference)
                    .destinationPitchArea(candidatePitchArea)
                    .build();
                if (candidateReceiver != null) {
                    disruption = disruption.toBuilder()
                        .receiver(candidateReceiver.getSimplifiedPlayerData())
                        .build();
                }
                return Optional.ofNullable(disruption);
            }
        }
        return Optional.empty();
    }

    private static Optional<DuelDisruption> handleMissedShot(Integer total) {

        if (total < MAX_TOTAL) {
            double difference = (double) (100 - total) / 4;
            int random = RandomGenerator.randomInt(0, 100);
            log.info("Total value = {} Difference value = {} and succeed = {}", total, difference, random);
            // Shot is missed
            if (random < difference) {
                return Optional.ofNullable(
                    DuelDisruption.builder()
                    .type(DuelDisruptor.MISSED_SHOT)
                    .difference(difference)
                    .build());
            }
        }

        return Optional.empty();
    }
}
