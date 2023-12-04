package com.kjeldsen.match.execution;

import com.kjeldsen.match.entities.Play;
import com.kjeldsen.match.entities.duel.DuelResult;
import com.kjeldsen.match.entities.duel.DuelRole;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.match.random.DuelRandomization;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.match.state.GameStateException;
import com.kjeldsen.match.entities.DuelStats;
import com.kjeldsen.match.entities.Player;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public class DuelExecution {

    /*
     * Determines the outcome of duels. Each duel execution method returns a DuelDTO.
     */

    public static DuelDTO executeDuel(DuelParams params) {
        // Before we execute the duel, player orders are given the opportunity to change of the
        // parameters for execution - the action (duelType) and challenger - as well as the skill
        // points of players involved (this is only temporary).
        DuelParams modifiedParams = params.getInitiator().getPlayerOrder().apply(params);

        return switch (modifiedParams.getDuelType()) {
            case POSITIONAL -> handlePositionalDuel(modifiedParams);
            case BALL_CONTROL -> handleBallControlDuel(modifiedParams);
            case PASSING -> handlePassDuel(modifiedParams);
            case SHOT -> handleShotDuel(modifiedParams);
        };
    }

    // Three things affect the positional duel outcome: (1) the skill points of the players, (2) the
    // performance of each player, which is randomly generated for each duel and (3) the assistance
    // each player gets from their teammates. All of these points are added and the player with the
    // highest sum wins the duel.
    public static DuelDTO handlePositionalDuel(DuelParams params) {
        GameState state = params.getState();
        Player initiator = params.getInitiator();
        Player challenger = params.getChallenger();

        // The range of team assistance values can be very high. We need the assistance for both
        // players first then the difference can be adjusted to a sensible value.
        Map<String, Integer> initiatorTeamAssistance =
            Assistance.teamAssistance(state, initiator, DuelRole.INITIATOR);
        Map<String, Integer> challengerTeamAssistance =
            Assistance.teamAssistance(state, challenger, DuelRole.CHALLENGER);
        // This is actual assistance given to the winner of the duel (the loser gets zero)
        Map<DuelRole, Integer> adjustedAssistance =
            Assistance.adjustAssistance(initiatorTeamAssistance, challengerTeamAssistance);

        DuelStats initiatorStats =
            buildPositionalDuelStats(
                state,
                initiator,
                DuelRole.INITIATOR,
                initiatorTeamAssistance,
                adjustedAssistance);

        DuelStats challengerStats;
        DuelResult result;
        // In some situations it's possible that there is no challenger present because of rules
        // preventing a defender from being into two consecutive duels. In this case the duel is
        // automatically won by the initiator.
        if (challenger == null) {
            int assistance =
                (int) (adjustedAssistance.get(DuelRole.CHALLENGER)
                    * Assistance.assistanceFactor(state, DuelRole.CHALLENGER));
            challengerStats = DuelStats.builder()
                .skillPoints(0)
                .performance(0)
                .assistance(assistance)
                .teamAssistance(challengerTeamAssistance)
                .total(50)
                .build();
            result = DuelResult.WIN;
        } else {
            challengerStats = buildPositionalDuelStats(
                state,
                challenger,
                DuelRole.CHALLENGER,
                challengerTeamAssistance,
                adjustedAssistance);
            result =
                (initiatorStats.getTotal() > challengerStats.getTotal())
                    ? DuelResult.WIN
                    : DuelResult.LOSE;
        }

        return DuelDTO.builder()
            .result(result)
            .initiatorStats(initiatorStats)
            .challengerStats(challengerStats)
            .origin(params.getOrigin())
            .params(params)
            .build();
    }

    // Performs the calculations for positional duels, which require assistance. The total in
    // DuelStats is used to determine the overall winner of the duel.
    private static DuelStats buildPositionalDuelStats(
        GameState state,
        Player player,
        DuelRole role,
        Map<String, Integer> teamAssistance,
        Map<DuelRole, Integer> adjustedAssistance) {

        int skillPoints = player.duelSkill(DuelType.POSITIONAL, role);
        int performance =
            DuelRandomization.performance(state, player, DuelType.POSITIONAL, role);

        int assistance =
            (int) (adjustedAssistance.get(role) * Assistance.assistanceFactor(state, role));

        int total = skillPoints + performance + assistance;

        return DuelStats.builder()
            .skillPoints(skillPoints)
            .performance(performance)
            .assistance(assistance)
            .teamAssistance(teamAssistance)
            .total(total)
            .build();
    }

    // Ball control follows a positional duel. Here the factors to determine a winner are (1) skill
    // points, (2) performance, and (3) the assistance carryover from the positional duel.
    public static DuelDTO handleBallControlDuel(DuelParams params) {
        GameState state = params.getState();
        Player initiator = params.getInitiator();
        Player challenger = params.getChallenger();

        DuelStats initiatorStats = buildDuelStats(
            state,
            initiator,
            DuelType.BALL_CONTROL,
            DuelRole.INITIATOR);

        DuelStats challengerStats = buildDuelStats(
            state,
            challenger,
            DuelType.BALL_CONTROL,
            DuelRole.CHALLENGER);

        DuelResult result =
            (initiatorStats.getTotal() > challengerStats.getTotal())
                ? DuelResult.WIN
                : DuelResult.LOSE;

        return DuelDTO.builder()
            .result(result)
            .initiatorStats(initiatorStats)
            .challengerStats(challengerStats)
            .origin(params.getOrigin())
            .params(params)
            .build();
    }

    // Passing duels always succeed for now but this leaves the possibility of an interception
    public static DuelDTO handlePassDuel(DuelParams params) {
        GameState state = params.getState();
        Player initiator = params.getInitiator();
        Player challenger = params.getChallenger();
        Player receiver = params.getReceiver();

        Optional.ofNullable(receiver)
            .orElseThrow(
                () -> new GameStateException(state, "A receiver was not set for a pass duel"));

        DuelStats initiatorStats = buildDuelStats(
            state,
            initiator,
            DuelType.PASSING,
            DuelRole.INITIATOR);

        DuelStats challengerStats = buildDuelStats(
            state,
            challenger,
            DuelType.PASSING,
            DuelRole.CHALLENGER);

        DuelResult result = DuelResult.WIN;

        return DuelDTO.builder()
            .result(result)
            .initiatorStats(initiatorStats)
            .challengerStats(challengerStats)
            .origin(params.getOrigin())
            .params(params)
            .build();
    }

    // A shot duel can follow many duel types. The contributing factors are (1) skill points, (2)
    // performance, and (3) the carryover, which is calculated from the total of the previous duel.
    public static DuelDTO handleShotDuel(DuelParams params) {
        GameState state = params.getState();
        Player initiator = params.getInitiator();
        Player challenger = params.getChallenger();

        DuelStats initiatorStats = buildDuelStats(
            state,
            initiator,
            DuelType.SHOT,
            DuelRole.INITIATOR);

        DuelStats challengerStats = buildDuelStats(
            state,
            challenger,
            DuelType.SHOT,
            DuelRole.CHALLENGER);

        DuelResult result =
            (initiatorStats.getTotal() > challengerStats.getTotal())
                ? DuelResult.WIN
                : DuelResult.LOSE;

        return DuelDTO.builder()
            .result(result)
            .initiatorStats(initiatorStats)
            .challengerStats(challengerStats)
            .origin(params.getOrigin())
            .params(params)
            .build();
    }

    // Performs the calculations for non-positional duels. These duels involve carry over rather
    // than assistance. The total in these stats is also used to determine the overall duel winner.
    private static DuelStats buildDuelStats(
        GameState state,
        Player player,
        DuelType type,
        DuelRole role) {

        int skillPoints = player.duelSkill(type, role);
        int performance = DuelRandomization.performance(state, player, type, role);

        if (type == DuelType.POSITIONAL) {
            throw new IllegalArgumentException("Positional duel not allowed here");
        }

        Map<DuelRole, Integer> carryovers = Carryover.getCarryover(state);
        int carryover = carryovers.get(role);

        int total = skillPoints + performance + carryover;

        return DuelStats.builder()
            .skillPoints(skillPoints)
            .performance(performance)
            .carryover(carryover)
            .total(total)
            .build();
    }

    // The number of consecutive losses for a player for a given duel type, counting from the most
    // recent play. Returns zero if the player has won the most recent duel of this type.
    public static int consecutiveLosses(GameState state, Player player, DuelType type) {
        return (int) state.getPlays().stream()
            .sorted(Comparator.comparingInt(Play::getClock).reversed())
            .map(Play::getDuel)
            .filter(
                duel -> duel.getInitiator().equals(player) || duel.getChallenger().equals(player))
            .filter(duel -> duel.getType() == type)
            .takeWhile(duel -> duel.getResult() == DuelResult.LOSE)
            .count();
    }
}
