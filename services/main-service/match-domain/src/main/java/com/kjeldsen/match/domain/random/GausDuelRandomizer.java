package com.kjeldsen.match.domain.random;

import com.kjeldsen.match.domain.entities.DuelStats;
import com.kjeldsen.match.domain.entities.Play;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.duel.DuelRole;
import com.kjeldsen.match.domain.entities.duel.DuelType;
import com.kjeldsen.match.domain.state.BallState;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class GausDuelRandomizer {

    private static final int SKILL_MIN_PERFORMANCE = 25;
    private static final int SKILL_MAX_PERFORMANCE = 75;

    public static final int MAX_PERFORMANCE = 15;
    public static final int MIN_PERFORMANCE = -15;
    private static final Random RANDOM = new Random();
    private static final List<Integer> SEQUENCE = new ArrayList<>();
    private static final List<Double> CDF = new ArrayList<>();
    private static final double[] DISTRIBUTION = {
            0.04, 0.05, 0.07, 0.10, 0.14, 0.19, 0.26, 0.34, 0.46, 0.59,
            0.76, 0.97, 1.21, 1.48, 1.79, 2.14, 2.50, 2.88, 3.27, 3.64,
            3.99, 4.29, 4.54, 4.71, 4.80, 4.80, 4.80, 4.71, 4.54, 4.29,
            3.99, 3.64, 3.27, 2.88, 2.50, 2.14, 1.79, 1.48, 1.21, 0.97,
            0.76, 0.59, 0.46, 0.34, 0.26, 0.19, 0.14, 0.10, 0.07, 0.05, 0.04
    };

    static {
        for (int i = 25; i <= 75; i++) {
            SEQUENCE.add(i);
        }
        double cumulative = 0.0;
        for (double prob : DISTRIBUTION) {
            cumulative += prob;
            CDF.add(cumulative);
        }
    }

    private static double mapToNewRange(double value) {
        double normalized = (value - SKILL_MIN_PERFORMANCE) / (SKILL_MAX_PERFORMANCE - SKILL_MIN_PERFORMANCE);
        return normalized * (MAX_PERFORMANCE - MIN_PERFORMANCE) + MIN_PERFORMANCE;
    }

    private static double generateGausModification() {
        double randomValue = RANDOM.nextDouble() * CDF.get(CDF.size() - 1);
        for (int i = 0; i < CDF.size(); i++) {
            if (randomValue <= CDF.get(i)) {
                return SEQUENCE.get(i);
            }
        }
        return SEQUENCE.get(SEQUENCE.size() - 1);
    }

    public static DuelStats.Performance performance(Player player, GameState state, DuelType type, DuelRole role) {
        List<PlayerSkill> requiredSkills = type.requiredSkills(role, state.getBallState().getHeight());
        Optional<DuelStats> prevDuel = previousDuel(state, player, requiredSkills, state.getBallState());
        Integer skillValue = player.duelSkill(type, role, state);

        double gausModification = Math.round(mapToNewRange(generateGausModification()) * 10.0 / 10.0);
        double previousDuelImpact = prevDuel.map(duelStats -> skillValue - duelStats.getTotal()).orElse(0);
        double modification = Math.round((gausModification + previousDuelImpact) * 10.0 / 10.0);

        return new DuelStats.Performance(previousDuelImpact, gausModification,
                Math.max(-15, Math.min(modification, 15)));
    }

    private static Optional<DuelStats> previousDuel(GameState state, Player player, List<PlayerSkill> skills,
            BallState ballState) {
        return state.getPlays().stream()
                .sorted(Comparator.comparingInt(Play::getClock).reversed()) // Sort by clock descending
                .map(Play::getDuel)
                .filter(Objects::nonNull)
                .filter(duel -> {
                    if (duel.getInitiator().getTeamRole() == player.getTeamRole()
                            && duel.getInitiator().getId().equals(player.getId())) {
                        return duel.getType().requiredSkills(DuelRole.INITIATOR, ballState.getHeight())
                                .containsAll(skills);
                    }
                    if (duel.getChallenger() != null && duel.getChallenger().getTeamRole() == player.getTeamRole()
                            && duel.getChallenger().getId().equals(player.getId())) {
                        return duel.getType().requiredSkills(DuelRole.CHALLENGER, ballState.getHeight())
                                .containsAll(skills);
                    }
                    return false;
                })
                .map(duel -> {
                    if (Objects.equals(duel.getInitiator().getId(), player.getId()) &&
                            duel.getInitiator().getTeamRole() == player.getTeamRole()) {
                        return duel.getInitiatorStats();
                    }
                    if (Objects.equals(duel.getChallenger().getId(), player.getId()) &&
                            duel.getChallenger().getTeamRole() == player.getTeamRole()) {
                        return duel.getChallengerStats();
                    }
                    throw new RuntimeException("Invalid duel state");
                })
                .findFirst();
    }
}
