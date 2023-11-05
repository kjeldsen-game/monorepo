package com.kjeldsen.match.engine.execution;

import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.engine.entities.Play;
import com.kjeldsen.match.engine.entities.duel.DuelResult;
import com.kjeldsen.match.engine.entities.duel.DuelRole;
import com.kjeldsen.match.engine.entities.duel.DuelStats;
import com.kjeldsen.match.engine.entities.duel.DuelType;
import com.kjeldsen.match.engine.exceptions.GameStateException;
import com.kjeldsen.match.engine.modifers.HorizontalPressure;
import com.kjeldsen.match.engine.modifers.VerticalPressure;
import com.kjeldsen.match.engine.random.DuelRandomization;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.engine.state.TeamState;
import com.kjeldsen.match.models.Player;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public class DuelExecution {

    /*
     * Determines the outcome of duels.
     *
     * TODO - refactor code repetition
     */

    public static DuelDTO executeDuel(
        GameState state,
        DuelType duelType,
        Player player,
        Player challenger,
        Player receiver) {

        return switch (duelType) {
            case POSITIONAL -> handlePositionalDuel(state, player, challenger);
            case BALL_CONTROL -> handleBallControlDuel(state, player, challenger);
            case PASSING -> handlePassDuel(state, player, challenger, receiver);
            case SHOT -> handleShotDuel(state, player, challenger);
        };
    }

    // Three things affect the positional duel outcome: (1) the skill points of the players, (2) the
    // performance of each player, which is randomly generated for each duel and (3) the assistance
    // each player gets from their teammates. All of these points are added and the player with the
    // highest sum wins the duel.
    public static DuelDTO handlePositionalDuel(
        GameState state, Player initiator, Player challenger) {
        // The range of team assistance values can be very high. We need the assistance for both
        // players first then we can normalize their sums to arrive at a sensible value.
        Map<String, Integer> initiatorTeamAssistance =
            Assistance.teamAssistance(state, initiator, DuelRole.INITIATOR);
        Map<String, Integer> challengerTeamAssistance =
            Assistance.teamAssistance(state, challenger, DuelRole.CHALLENGER);

        Map<DuelRole, Integer> normalizedAssistanceTotals =
            Assistance.normalizeAssistance(
                initiatorTeamAssistance, challengerTeamAssistance);

        int initiatorSkillPoints = initiator.duelSkill(DuelType.POSITIONAL, DuelRole.INITIATOR);
        int initiatorPerformance =
            DuelRandomization.performance(
                state, initiator, DuelType.POSITIONAL, DuelRole.INITIATOR);
        int initiatorAssistance = normalizedAssistanceTotals.get(DuelRole.INITIATOR);
        int initiatorTotal = initiatorSkillPoints + initiatorPerformance + initiatorAssistance;

        int challengerSkillPoints = challenger.duelSkill(DuelType.POSITIONAL, DuelRole.CHALLENGER);
        int challengerPerformance =
            DuelRandomization.performance(
                state, challenger, DuelType.POSITIONAL, DuelRole.CHALLENGER);

        double assistanceModifier =
            1 + defensiveAssistanceModifier(state.getBallState().getArea(), state.defendingTeam());
        int challengerAssistance =
            (int) (normalizedAssistanceTotals.get(DuelRole.CHALLENGER) * assistanceModifier);

        int challengerTotal =
            challengerSkillPoints + challengerPerformance + challengerAssistance;

        DuelResult result = (initiatorTotal > challengerTotal) ? DuelResult.WIN : DuelResult.LOSE;

        return DuelDTO.builder()
            .result(result)
            .initiatorStats(
                DuelStats.builder()
                    .skillPoints(initiatorSkillPoints)
                    .performance(initiatorPerformance)
                    .assistance(initiatorAssistance)
                    .teamAssistance(initiatorTeamAssistance)
                    .total(initiatorTotal)
                    .build())
            .challengerStats(
                DuelStats.builder()
                    .skillPoints(challengerSkillPoints)
                    .performance(challengerPerformance)
                    .assistance(challengerAssistance)
                    .teamAssistance(challengerTeamAssistance)
                    .total(challengerTotal)
                    .build())
            .build();
    }

    // Defensive assistance is affected by the vertical and horizontal pressure modifiers.
    // Returns a factor that increases/decreases the assistance value.
    private static double defensiveAssistanceModifier(PitchArea area, TeamState teamState) {
        VerticalPressure vertical = teamState.getVerticalPressure();
        HorizontalPressure horizontal = teamState.getHorizontalPressure();
        double verticalBonus = vertical.defensiveAssistanceBonus(area);
        double horizontalBonus = horizontal.defensiveAssistanceBonus(area);
        return verticalBonus + horizontalBonus;
    }

    // Ball control follows a positional duel. Here the factors to determine a winner are (1) skill
    // points, (2) performance, and (3) the assistance carryover from the positional duel.
    public static DuelDTO handleBallControlDuel(
        GameState state, Player initiator, Player challenger) {

        int initiatorSkillPoints = initiator.duelSkill(DuelType.BALL_CONTROL, DuelRole.INITIATOR);
        int initiatorPerformance =
            DuelRandomization.performance(
                state, initiator, DuelType.BALL_CONTROL, DuelRole.INITIATOR);
        int initiatorTotal = initiatorSkillPoints + initiatorPerformance;

        int challengerSkillPoints = challenger.duelSkill(
            DuelType.BALL_CONTROL, DuelRole.CHALLENGER);
        int challengerPerformance =
            DuelRandomization.performance(
                state, challenger, DuelType.BALL_CONTROL, DuelRole.CHALLENGER);
        int challengerTotal = challengerSkillPoints + challengerPerformance;

        // Positive carry over means the initiator won the previous duel
        int carryover = Carryover.fromPositionalDuel(state);
        DuelRole carryoverReceiver = carryover > 0 ? DuelRole.INITIATOR : DuelRole.CHALLENGER;
        if (carryoverReceiver == DuelRole.INITIATOR) {
            initiatorTotal += carryover;
        } else {
            challengerTotal -= carryover;
        }
        carryover = Math.abs(carryover);

        DuelResult result = (initiatorTotal > challengerTotal) ? DuelResult.WIN : DuelResult.LOSE;

        return DuelDTO.builder()
            .result(result)
            .initiatorStats(
                DuelStats.builder()
                    .skillPoints(initiatorSkillPoints)
                    .performance(initiatorPerformance)
                    .carryover(carryoverReceiver == DuelRole.INITIATOR ? carryover : null)
                    .total(initiatorTotal)
                    .build())
            .challengerStats(
                DuelStats.builder()
                    .skillPoints(challengerSkillPoints)
                    .performance(challengerPerformance)
                    .carryover(carryoverReceiver == DuelRole.CHALLENGER ? carryover : null)
                    .total(challengerTotal)
                    .build())
            .build();
    }

    // Passing duels always succeed for now but this leaves the possibility of an interception
    public static DuelDTO handlePassDuel(
        GameState state, Player initiator, Player challenger, Player receiver) {

        Optional.ofNullable(receiver)
            .orElseThrow(
                () -> new GameStateException(state, "A receiver was not set for a pass duel"));

        int initiatorSkillPoints = initiator.duelSkill(DuelType.PASSING, DuelRole.INITIATOR);
        int initiatorPerformance =
            DuelRandomization.performance(
                state, initiator, DuelType.PASSING, DuelRole.INITIATOR);
        int initiatorTotal = initiatorSkillPoints + initiatorPerformance;

        int challengerSkillPoints = challenger.duelSkill(DuelType.PASSING, DuelRole.CHALLENGER);
        int challengerPerformance =
            DuelRandomization.performance(
                state, challenger, DuelType.PASSING, DuelRole.CHALLENGER);
        int challengerTotal = challengerSkillPoints + challengerPerformance;

        DuelResult result = DuelResult.WIN;

        return DuelDTO.builder()
            .initiatorStats(
                DuelStats.builder()
                    .skillPoints(initiatorSkillPoints)
                    .performance(initiatorPerformance)
                    .total(initiatorTotal)
                    .build())
            .challengerStats(
                DuelStats.builder()
                    .skillPoints(challengerSkillPoints)
                    .performance(challengerPerformance)
                    .total(challengerTotal)
                    .build())
            .result(result).build();
    }

    // A shot duel can follow many duel types. The contributing factors are (1) skill points, (2)
    // performance, and (3) the carryover, which is calculated from the total of the previous duel.
    public static DuelDTO handleShotDuel(GameState state, Player initiator, Player goalkeeper) {
        int initiatorSkillPoints = initiator.duelSkill(DuelType.SHOT, DuelRole.INITIATOR);
        int initiatorPerformance =
            DuelRandomization.performance(
                state, initiator, DuelType.SHOT, DuelRole.INITIATOR);
        int initiatorTotal = initiatorSkillPoints + initiatorPerformance;

        int challengerSkillPoints = goalkeeper.duelSkill(DuelType.SHOT, DuelRole.CHALLENGER);
        int challengerPerformance =
            DuelRandomization.performance(
                state, goalkeeper, DuelType.SHOT, DuelRole.CHALLENGER);
        int challengerTotal = challengerSkillPoints + challengerPerformance;

        // Positive carry over means the initiator won the previous duel
        int carryover = Carryover.fromPreviousDuel(state);
        DuelRole carryoverReceiver = carryover > 0 ? DuelRole.INITIATOR : DuelRole.CHALLENGER;
        if (carryoverReceiver == DuelRole.INITIATOR) {
            initiatorTotal += carryover;
        } else {
            challengerTotal -= carryover;
        }
        carryover = Math.abs(carryover);

        DuelResult result = (initiatorTotal > challengerTotal) ? DuelResult.WIN : DuelResult.LOSE;
        return DuelDTO.builder()
            .initiatorStats(
                DuelStats.builder()
                    .skillPoints(initiatorSkillPoints)
                    .performance(initiatorPerformance)
                    .carryover(carryoverReceiver == DuelRole.INITIATOR ? carryover : null)
                    .total(initiatorTotal)
                    .build())
            .challengerStats(
                DuelStats.builder()
                    .skillPoints(challengerSkillPoints)
                    .performance(challengerPerformance)
                    .carryover(carryoverReceiver == DuelRole.CHALLENGER ? carryover : null)
                    .total(challengerTotal)
                    .build())
            .result(result)
            .build();
    }

    // The number of consecutive losses for a player for a given duel type, counting from the most
    // recent play. Returns zero if the player has won the most recent duel of this type.
    public static int consecutiveLosses(GameState state, Player player, DuelType duelType) {
        return (int) state.getPlays().stream()
            .sorted(Comparator.comparingInt(Play::getMinute).reversed())
            .map(Play::getDuel)
            .filter(
                duel -> duel.getInitiator().equals(player) || duel.getChallenger().equals(player))
            .filter(duel -> duel.getType() == duelType)
            .takeWhile(duel -> duel.getResult() == DuelResult.LOSE)
            .count();
    }
}
