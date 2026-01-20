package com.kjeldsen.match.domain.execution;

import com.kjeldsen.match.domain.entities.DuelStats;
import com.kjeldsen.match.domain.entities.Play;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.duel.DuelRole;
import com.kjeldsen.match.domain.entities.duel.DuelType;
import com.kjeldsen.match.domain.random.GausDuelRandomizer;
import com.kjeldsen.match.domain.state.ChainActionSequence;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.player.domain.PitchArea;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class DuelStatsBuilder {

    /**
     * Performs the calculations for positional duels, which require assistance. The
     * total in DuelStats is used to determine the overall winner of the duel.
     */
    public static DuelStats buildPositionalDuelStats(
        GameState state,
        Player player,
        DuelRole role,
        DuelStats.Assistance assistance) {

        int skillPoints = player.duelSkill(DuelType.POSITIONAL, role, state);
        DuelStats.Performance performance = GausDuelRandomizer.performance(player, state, DuelType.POSITIONAL, role);

        // Apply chain action sequences skill modifiers.
        int chainActionSequenceModifier = 0;

        Optional<Play> previousPlay = state.lastPlay();
        if (role.equals(DuelRole.CHALLENGER) && previousPlay.isPresent()
            && previousPlay.get().getDuel().getDuelDisruption() != null
            && previousPlay.get().getDuel().getDuelDisruption().getDestinationPitchArea() != PitchArea.OUT_OF_BOUNDS) {
            assistance.getModifiers().put(ChainActionSequence.MISSED_PASS, 50);
            assistance.setTotal(assistance.getTotal() + assistance.getModifiersSum());
        }

        int total = skillPoints + chainActionSequenceModifier + performance.getTotal().intValue() +
            assistance.getAdjusted().intValue() + assistance.getModifiersSum().intValue();

        return DuelStats.builder()
            .skillPoints(skillPoints)
            .performance(performance)
            .assistance(assistance)
            .total(total)
            .build();
    }

    // Performs the calculations for non-positional duels. These duels involve carry
    // over rather
    // than assistance. The total in these stats is also used to determine the
    // overall duel winner.
    public static DuelStats buildDuelStats(GameState state, Player player, DuelType type, DuelRole role) {
        // Case for the THROW_IN because challenger is not present
        if (type.equals(DuelType.THROW_IN) && role.equals(DuelRole.CHALLENGER)) {
            return DuelStats.initWithoutAssistance();
        }

        if (type == DuelType.POSITIONAL) {
            throw new IllegalArgumentException("Positional duel not allowed here");
        }

        int skillPoints = player.duelSkill(type, role, state);
        DuelStats.Performance performance = GausDuelRandomizer.performance(player, state, type, role);

        Map<DuelRole, Integer> carryovers = Carryover.getCarryover(state);
        int carryover = carryovers.get(role);

        int total = skillPoints + performance.getTotal().intValue() + carryover;

        return DuelStats.builder()
            .skillPoints(skillPoints)
            .performance(performance)
            .carryover(carryover)
            .total(total)
            .build();
    }

}
