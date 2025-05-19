package com.kjeldsen.match.domain.random;

import com.kjeldsen.match.domain.entities.Play;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.duel.Duel;
import com.kjeldsen.match.domain.entities.duel.DuelRole;
import com.kjeldsen.match.domain.state.BallState;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.player.domain.PlayerSkill;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

// TODO cleanup

public class DuelRandomization {

    public static final int MAX_PERFORMANCE = 15;
    public static final int MIN_PERFORMANCE = -15;

    public static final int MAX_ASSISTANCE = 25;
    public static final int MIN_ASSISTANCE = 0;

    // Probability of a player with total points `a` winning a duel against a player
    // with level `b`.
    // These points include the modifier and assistance.
    public static double initialWinProbability(int a, int b) {
        // Difference in rating yields a number between -140 and 140
        double diff = a - b;
        // Normalize this number to get a probability value
        double min = (Player.MIN_SKILL_VALUE + MIN_PERFORMANCE + MIN_ASSISTANCE)
                - Player.MAX_SKILL_VALUE
                - MAX_PERFORMANCE
                - MAX_ASSISTANCE;
        double max = (Player.MAX_SKILL_VALUE + MAX_PERFORMANCE + MAX_ASSISTANCE)
                - Player.MIN_SKILL_VALUE
                - MIN_PERFORMANCE
                - MIN_ASSISTANCE;
        return (diff - min) / (max - min);
    }

    // A randomly generated number that represents how well the player performed in
    // the duel.
    // A player's experience modifies the range of the performance - more
    // experienced players have a
    // smaller and higher range of performance values. This value is also adjusted
    // based on their
    // performance in previous duels to avoid too many consecutive wins/losses.
    // public static int performance(GameState state, Player player, DuelType type,
    // DuelRole role) {
    // List<PlayerSkill> requiredSkills = type.requiredSkills(role,
    // state.getBallState().getHeight());
    // // TODO - experience adjustment
    // double adjustment = previousDuel(state, player, requiredSkills,
    // state.getBallState())
    // .map(duel -> {
    // if (role == DuelRole.INITIATOR) {
    // return duel.getInitiatorStats().getPerformance();
    // } else {
    // return duel.getChallengerStats().getPerformance();
    // }
    // })
    // .orElse(new P);
    //
    // int performance = new Random().nextInt(MIN_PERFORMANCE, MAX_PERFORMANCE + 1);
    // return Math.min(performance + (int) adjustment, MAX_PERFORMANCE);
    // }

    // Returns the previous duel that involved a particular skill for the given
    // player
    public static Optional<Duel> previousDuel(GameState state, Player player, List<PlayerSkill> skills,
            BallState ballState) {
        return state.getPlays().stream()
                .sorted(Comparator.comparingInt(Play::getClock).reversed())
                .map(Play::getDuel)
                .filter(duel -> {
                    if (Objects.equals(duel.getInitiator().getId(), player.getId())) {
                        return duel.getType().requiredSkills(DuelRole.INITIATOR, ballState.getHeight())
                                .containsAll(skills);
                    }
                    if (duel.getChallenger() != null) {
                        if (Objects.equals(duel.getChallenger().getId(), player.getId())) {
                            return duel.getType().requiredSkills(DuelRole.CHALLENGER, ballState.getHeight())
                                    .containsAll(skills);
                        }
                    }
                    return false;
                })
                .findFirst();
    }
}
