package com.kjeldsen.match.domain.execution;

import com.kjeldsen.match.domain.entities.DuelStats;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.duel.DuelRole;
import com.kjeldsen.match.domain.state.BallState;
import com.kjeldsen.match.domain.state.ChainActionSequence;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.match.domain.state.TeamState;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Slf4j
public class AssistanceProvider {
    public static final List<Double> TEAM_ASSISTANCE_SCALING = Arrays.asList(
        1.00, 1.94, 2.82, 3.65, 4.43, 5.17, 5.86, 6.51, 7.12, 7.69,
        8.23, 8.73, 9.21, 9.66, 10.08, 10.47, 10.85, 11.19, 11.52, 11.83,
        12.12, 12.39, 12.65, 12.89, 13.12, 13.33, 13.53, 13.72, 13.90, 14.06,
        14.22, 14.37, 14.50, 14.63, 14.76, 14.87, 14.98, 15.08, 15.17, 15.26,
        15.35, 15.43, 15.50, 15.57, 15.64, 15.70, 15.76, 15.81, 15.86, 15.91,
        15.96, 16.00, 16.04, 16.08, 16.11, 16.15, 16.18, 16.21, 16.23, 16.26,
        16.28, 16.31, 16.33, 16.35, 16.37, 16.39, 16.40, 16.42, 16.43, 16.45,
        16.46, 16.47, 16.48, 16.50, 16.51, 16.52, 16.52, 16.53, 16.54, 16.55,
        16.56, 16.56, 16.57, 16.57, 16.58, 16.59, 16.59, 16.59, 16.60, 16.60,
        16.61, 16.61, 16.61, 16.62, 16.62, 16.62, 16.63, 16.63, 16.63, 16.63,
        16.63, 16.64, 16.64, 16.64, 16.64, 16.64
    );

    public static final Map<PlayerPosition, List<Double>> INITIATOR_ASSISTANCE_MODIFIER_MAP = Map.ofEntries(
        Map.entry(PlayerPosition.GOALKEEPER, List.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)),
        Map.entry(PlayerPosition.CENTRE_BACK, List.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)),
        Map.entry(PlayerPosition.RIGHT_BACK, List.of(0.0, 0.0, 0.0, 0.0, 0.25, 0.75, 0.0, 0.0, 0.25)),
        Map.entry(PlayerPosition.LEFT_BACK, List.of(0.0, 0.0, 0.0, 0.75, 0.25, 0.0, 0.25, 0.0, 0.0)),
        Map.entry(PlayerPosition.RIGHT_WINGBACK, List.of(0.0, 0.0, 0.0, 0.25, 0.5, 1.0, 0.0, 0.0, 0.75)),
        Map.entry(PlayerPosition.LEFT_WINGBACK, List.of(0.0, 0.0, 0.0, 1.0, 0.5, 0.25, 0.75, 0.0, 0.0)),
        Map.entry(PlayerPosition.DEFENSIVE_MIDFIELDER, List.of(0.0, 0.0, 0.0, 0.375, 0.5, 0.375, 0.0, 0.0, 0.0)),
        Map.entry(PlayerPosition.CENTRE_MIDFIELDER, List.of(0.0, 0.0, 0.0, 0.5, 1.0, 0.5, 0.375, 0.625, 0.375)),
        Map.entry(PlayerPosition.RIGHT_MIDFIELDER, List.of(0.0, 0.0, 0.0, 0.25, 0.5, 1.0, 0.0, 0.5, 0.75)),
        Map.entry(PlayerPosition.LEFT_MIDFIELDER, List.of(0.0, 0.0, 0.0, 1.0, 0.5, 0.25, 0.75, 0.5, 0.0)),
        Map.entry(PlayerPosition.RIGHT_WINGER, List.of(0.0, 0.0, 0.0, 0.0, 0.375, 0.75, 0.25, 0.75, 1.0)),
        Map.entry(PlayerPosition.LEFT_WINGER, List.of(0.0, 0.0, 0.0, 0.75, 0.375, 0.0, 1.0, 0.75, 0.25)),
        Map.entry(PlayerPosition.OFFENSIVE_MIDFIELDER, List.of(0.0, 0.0, 0.0, 0.75, 1.25, 0.75, 0.5, 0.75, 0.5)),
        Map.entry(PlayerPosition.FORWARD, List.of(0.0, 0.0, 0.0, 0.0, 0.25, 0.0, 0.5, 1.0, 0.5))
    );

    public static final Map<PlayerPosition, List<Double>> CHALLENGER_ASSISTANCE_MODIFIER_MAP = Map.ofEntries(
        Map.entry(PlayerPosition.GOALKEEPER, List.of(0.5, 1.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)),
        Map.entry(PlayerPosition.CENTRE_BACK, List.of(0.5, 1.0, 0.5, 0.0, 0.25, 0.0, 0.0, 0.0, 0.0)),
        Map.entry(PlayerPosition.RIGHT_BACK, List.of(0.0, 0.75, 1.0, 0.0, 0.375, 1.0, 0.0, 0.0, 0.0)),
        Map.entry(PlayerPosition.LEFT_BACK, List.of(1.0, 0.75, 0.0, 1.0, 0.375, 0.0, 0.0, 0.0, 0.0)),
        Map.entry(PlayerPosition.RIGHT_WINGBACK, List.of(0.0, 0.5, 0.75, 0.0, 0.5, 1.0, 0.0, 0.0, 0.0)),
        Map.entry(PlayerPosition.LEFT_WINGBACK, List.of(0.75, 0.5, 0.0, 1.0, 0.5, 0.0, 0.0, 0.0, 0.0)),
        Map.entry(PlayerPosition.DEFENSIVE_MIDFIELDER, List.of(0.5, 0.75, 0.5, 1.0, 1.25, 1.0, 0.0, 0.0, 0.0)),
        Map.entry(PlayerPosition.CENTRE_MIDFIELDER, List.of(0.375, 0.5, 0.375, 0.75, 1.0, 0.75, 0.0, 0.0, 0.0)),
        Map.entry(PlayerPosition.RIGHT_MIDFIELDER, List.of(0.0, 0.0, 0.75, 0.25, 0.75, 1.0, 0.0, 0.0, 0.0)),
        Map.entry(PlayerPosition.LEFT_MIDFIELDER, List.of(0.75, 0.0, 0.0, 1.0, 0.75, 0.25, 0.0, 0.0, 0.0)),
        Map.entry(PlayerPosition.RIGHT_WINGER, List.of(0.0, 0.0, 0.375, 0.0, 0.5, 0.75, 0.0, 0.0, 0.0)),
        Map.entry(PlayerPosition.LEFT_WINGER, List.of(0.375, 0.0, 0.0, 0.75, 0.5, 0.0, 0.0, 0.0, 0.0)),
        Map.entry(PlayerPosition.OFFENSIVE_MIDFIELDER, List.of(0.0, 0.0, 0.0, 0.375, 0.5, 0.375, 0.0, 0.0, 0.0)),
        Map.entry(PlayerPosition.FORWARD, List.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
    );


    /**
     * Calculate the team assistance for team for positional duel.
     *
     * @param state State of the game.
     * @param player Player that is in the duel.
     * @param role Role of the player in the duel.
     * @return The computed map of the assistance in duel.
     */
    public static Map<String, Double> getTeamAssistance(GameState state, Player player, DuelRole role) {

        TeamState teamState = TeamState.getTeamByRole(state, role);

        return teamState.getPlayers().stream()
            .filter(teammate -> !teammate.equals(player))
            .collect(Collectors.toMap(
                Player::getName,
                teammate -> getPlayerAssistanceValue(teammate, state.getBallState(), role)))
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() > 0)
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue));
    }

    /**
     * Compute the assistance for the player in positional duel based on ballState and role of the player.
     *
     * @param player Player which one is used in the calculation
     * @param ballState Ball state in the duel
     * @param role Role of the player in the duel
     * @return The computed Double value for the player assistance
     */
    private static Double getPlayerAssistanceValue(Player player, BallState ballState, DuelRole role) {

        PitchArea area = ballState.getArea();
        Map<PlayerSkill, Integer> skills = player.getSkills();
        Double modifier;
        double skillsValue;

        return switch (role) {
            case CHALLENGER -> {
                modifier = CHALLENGER_ASSISTANCE_MODIFIER_MAP.get(player.getPosition()).get(area.ordinal());
                skillsValue = player.getPosition().equals(PlayerPosition.GOALKEEPER) ?
                    (double) skills.get(PlayerSkill.ORGANIZATION) :
                    (double) skills.get(PlayerSkill.DEFENSIVE_POSITIONING) + player.getSkills().get(PlayerSkill.TACKLING);
                yield skillsValue * modifier;
            }
            case INITIATOR -> {
                modifier = INITIATOR_ASSISTANCE_MODIFIER_MAP.get(player.getPosition()).get(area.flipPerspective().ordinal());
                skillsValue = player.getPosition().equals(PlayerPosition.GOALKEEPER) ?
                    (double) skills.get(PlayerSkill.ORGANIZATION) :
                    (double) skills.get(PlayerSkill.OFFENSIVE_POSITIONING) + player.getSkills().get(PlayerSkill.BALL_CONTROL);
                yield skillsValue * modifier;
            }
        };
    }

    /**
     * Adjust the team assistance after difference in duel is calculated based on scaling rules.
     *
     * @param assistanceDifference Value of the team assistance difference in the duel.
     * @return Adjusted team assistance value.
     * */
    private static Double getAdjustedAssistance(Double assistanceDifference) {

        int absAssistanceValue = (int) Math.abs(assistanceDifference);

        if (absAssistanceValue <= 0) {
            return TEAM_ASSISTANCE_SCALING.get(0);
        } else if (absAssistanceValue >= TEAM_ASSISTANCE_SCALING.size() - 1) {
            return TEAM_ASSISTANCE_SCALING.get(TEAM_ASSISTANCE_SCALING.size() - 1);
        } else {
            return TEAM_ASSISTANCE_SCALING.get(absAssistanceValue);
        }
    }

    /**
     * Compute he adjusted team assistance by duel role, winner get scaled value otherwise 0.
     *
     * @param initiatorAssistance Initiator team assistance map
     * @param challengerAssistance Challenger team assistance map
     * @return Assistance object with total and adjusted value.
     * */
    public static Map<DuelRole, DuelStats.Assistance> buildAssistanceByDuelRole(
        Map<String, Double> initiatorAssistance,
        Map<String, Double> challengerAssistance,
        Map<ChainActionSequence, Integer> initiatorModifiers) {

        Map<DuelRole, DuelStats.Assistance> assistanceByRole = new HashMap<>();

        double initiatorModifierTotal = initiatorModifiers.values().stream()
            .mapToDouble(Integer::doubleValue).sum();
        double challengerModifierTotal = 0.0;

        double initiatorTotal = initiatorAssistance.values().stream()
            .mapToDouble(Double::doubleValue).sum() + initiatorModifierTotal;
        double challengerTotal = challengerAssistance.values().stream()
            .mapToDouble(Double::doubleValue).sum() + challengerModifierTotal;

        double assistanceDifference = getAdjustedAssistance(initiatorTotal - challengerTotal);

        BiFunction<Map<String, Double>, DuelRole, DuelStats.Assistance> build = (teamAssistance, role) -> {
            boolean isInitiator = role == DuelRole.INITIATOR;
            double totalModifiers = isInitiator ? initiatorModifierTotal : challengerModifierTotal;
            Map<ChainActionSequence, Integer> modifiers = isInitiator ? initiatorModifiers : new HashMap<>();
            double total = isInitiator ? initiatorTotal : challengerTotal;
            double adjusted = isInitiator
                ? Math.max(assistanceDifference, 0.0)
                : Math.max(-assistanceDifference, 0.0);
            return DuelStats.Assistance.builder()
                .modifiers(modifiers)
                .totalModifiers(totalModifiers)
                .teamAssistance(teamAssistance)
                .total(total)
                .adjusted(adjusted)
                .build();
        };

        assistanceByRole.put(DuelRole.INITIATOR, build.apply(initiatorAssistance, DuelRole.INITIATOR));
        assistanceByRole.put(DuelRole.CHALLENGER, build.apply(challengerAssistance, DuelRole.CHALLENGER));

        return assistanceByRole;
    }
}
