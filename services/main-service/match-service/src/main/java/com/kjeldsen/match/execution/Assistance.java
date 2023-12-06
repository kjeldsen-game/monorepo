package com.kjeldsen.match.execution;

import com.kjeldsen.match.entities.Player;
import com.kjeldsen.match.entities.duel.DuelRole;
import com.kjeldsen.match.modifers.HorizontalPressure;
import com.kjeldsen.match.modifers.Tactic;
import com.kjeldsen.match.modifers.VerticalPressure;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.match.state.TeamState;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PitchArea.PitchRank;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Assistance {

    /*
     * Determines the assistance a player gets from his teammates in a positional duel.
     */

    public static final int MAX_ASSISTANCE = 25;
    public static final int MIN_ASSISTANCE = 0;
    public static double BALL_CONTROL_FACTOR = 0.9;
    public static double TACKLING_FACTOR = 0.9;

    // Calculates the assistance each player on the team provides to the player in the duel
    public static Map<String, Integer> teamAssistance(
        GameState state, Player player, DuelRole role) {

        PitchArea pitchArea = state.getBallState().getArea();
        TeamState team = getTeam(state, role);
        return team.getPlayers().stream()
            .filter(teammate -> !teammate.equals(player))
            .collect(Collectors.toMap(
                Player::getName,
                teammate -> playerAssistance(team.getTactic(), teammate, role, pitchArea)))
            .entrySet()
            .stream()
            .filter(assistance -> assistance.getValue() > 0)
            .collect(Collectors.toMap(
                Entry::getKey,
                Map.Entry::getValue));
    }

    // Calculates the assistance an individual player provides to the teammate in the duel
    private static int playerAssistance(
        Tactic tactic, Player player, DuelRole role, PitchArea ballArea) {

        if (player.getPosition() == PlayerPosition.GOALKEEPER) {
            return 0;
        }
        return switch (role) {
            case INITIATOR -> {
                double supportFactor = attackerSupport(player.getPosition(), ballArea);
                double op = player.getSkills().get(PlayerSkill.OFFENSIVE_POSITIONING);
                double bc =
                    player.getSkills().get(PlayerSkill.BALL_CONTROL) * BALL_CONTROL_FACTOR;
                double tacticFactor =
                    tactic.assistanceFactor(player.getPosition(), DuelRole.INITIATOR, ballArea);
                yield (int) ((op + bc) * supportFactor * tacticFactor);
            }

            case CHALLENGER -> {
                double supportFactor = defenderSupport(player.getPosition(), ballArea);
                double dp = player.getSkills().get(PlayerSkill.DEFENSIVE_POSITIONING);
                double ta = player.getSkills().get(PlayerSkill.TACKLING) * TACKLING_FACTOR;
                yield (int) (supportFactor * (dp + ta));
            }
        };
    }

    // Takes the assistance that two players in a duel receive and adjusts the difference to a
    // value between the constants defined in this class. This is done by taking the square root of
    // the difference and limiting it to the maximum assistance.
    public static Map<DuelRole, Integer> adjustAssistance(
        Map<String, Integer> initiatorAssistance, Map<String, Integer> challengerAssistance) {

        int initiatorTotal =
            initiatorAssistance.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        int challengerTotal =
            challengerAssistance.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        // Adjust the difference and give it to winner. The loser gets 0.
        double difference = Math.abs(initiatorTotal - challengerTotal);
        // The assistance is capped, but the actual value will rarely come near the limit.
        int assistance = (int) Math.min(Math.sqrt(difference), MAX_ASSISTANCE);
        DuelRole winner =
            (initiatorTotal > challengerTotal) ? DuelRole.INITIATOR : DuelRole.CHALLENGER;
        DuelRole loser = (winner == DuelRole.INITIATOR) ? DuelRole.CHALLENGER : DuelRole.INITIATOR;

        return Map.of(
            winner, assistance,
            loser, 0
        );
    }

    // Assistance is affected by the vertical and horizontal pressure modifiers. These modifiers are
    // expressed as a percentage by which to increase/decrease assistance. To get the factor here we
    // add 1 to the modifier, then it can be multiplied by the base assistance value.
    public static double assistanceFactor(GameState state, DuelRole role) {
        // Horizontal pressure and vertical pressure are presently only for defensive assistance.
        if (role == DuelRole.INITIATOR) {
            return 1.0;
        }
        PitchArea area = state.getBallState().getArea();
        TeamState teamState = state.defendingTeam();
        VerticalPressure vertical = teamState.getVerticalPressure();
        HorizontalPressure horizontal = teamState.getHorizontalPressure();
        double verticalModifier = vertical.defensiveAssistanceModifier(area);
        double horizontalModifier = horizontal.defensiveAssistanceModifier(area);
        return verticalModifier + horizontalModifier;
    }

    // Factor by which assistance is multiplied by for the attacking player
    private static double attackerSupport(PlayerPosition position, PitchArea area) {
        if (area.rank() == PitchRank.FORWARD) {
            return switch (area.file()) {
                case LEFT, RIGHT -> flankForwardAttackerSupport(position);
                case CENTRE -> centerForwardAttackerSupport(position);
            };
        }
        if (area.rank() == PitchRank.MIDDLE) {
            return switch (area.file()) {
                case LEFT, RIGHT -> flankMidfieldAttackerSupport(position);
                case CENTRE -> centerMidfieldAttackerSupport(position);
            };
        }

        return 0.0;
    }

    // Factor by which assistance is multiplied by for the defending player
    private static double defenderSupport(PlayerPosition position, PitchArea area) {
        if (area.rank() == PitchRank.FORWARD) {
            return switch (area.file()) {
                case LEFT, RIGHT -> flankForwardDefenderSupport(position);
                case CENTRE -> centerForwardDefenderSupport(position);
            };
        }
        if (area.rank() == PitchRank.MIDDLE) {
            return switch (area.file()) {
                case LEFT, RIGHT -> flankMidfieldDefenderSupport(position);
                case CENTRE -> centerMidfieldDefenderSupport(position);
            };
        }

        return 0.0;
    }

    private static double flankForwardAttackerSupport(PlayerPosition position) {
        return switch (position) {
            case LEFT_WINGER -> 1.0;
            case LEFT_MIDFIELDER -> 0.75;
            case LEFT_WINGBACK -> 0.75;
            case LEFT_BACK -> 0.5;
            case OFFENSIVE_MIDFIELDER -> 0.5;
            case FORWARD -> 0.5;
            case CENTRE_MIDFIELDER -> 0.35;
            case RIGHT_WINGER -> 1.0;
            case RIGHT_MIDFIELDER -> 0.75;
            case RIGHT_WINGBACK -> 0.75;
            case RIGHT_BACK -> 0.5;
            default -> 0.0;
        };
    }

    private static double centerForwardAttackerSupport(PlayerPosition position) {
        return switch (position) {
            case LEFT_WINGER -> 0.75;
            case LEFT_MIDFIELDER -> 0.5;
            case OFFENSIVE_MIDFIELDER -> 0.75;
            case FORWARD -> 1.0;
            case CENTRE_MIDFIELDER -> 0.5;
            case RIGHT_WINGER -> 0.75;
            case RIGHT_MIDFIELDER -> 0.5;
            default -> 0.0;
        };
    }


    private static double flankMidfieldAttackerSupport(PlayerPosition position) {
        return switch (position) {
            case LEFT_WINGER -> 1.0;
            case LEFT_MIDFIELDER -> 1.0;
            case LEFT_WINGBACK -> 1.0;
            case LEFT_BACK -> 0.75;
            case OFFENSIVE_MIDFIELDER -> 0.75;
            case CENTRE_MIDFIELDER -> 0.5;
            case DEFENSIVE_MIDFIELDER -> 0.35;
            case RIGHT_WINGER -> 1.0;
            case RIGHT_MIDFIELDER -> 1.0;
            case RIGHT_WINGBACK -> 1.0;
            case RIGHT_BACK -> 0.75;
            default -> 0.0;
        };
    }


    private static double centerMidfieldAttackerSupport(PlayerPosition position) {
        return switch (position) {
            case LEFT_WINGER -> 0.35;
            case LEFT_MIDFIELDER -> 0.75;
            case OFFENSIVE_MIDFIELDER -> 1.25;
            case FORWARD -> 0.25;
            case CENTRE_MIDFIELDER -> 1.0;
            case DEFENSIVE_MIDFIELDER -> 0.5;
            case RIGHT_WINGER -> 0.35;
            case RIGHT_MIDFIELDER -> 0.75;
            default -> 0.0;
        };
    }

    private static double flankForwardDefenderSupport(PlayerPosition position) {
        return switch (position) {
            case LEFT_WINGER -> 0.35;
            case LEFT_MIDFIELDER -> 0.35;
            case LEFT_WINGBACK -> 0.75;
            case LEFT_BACK -> 1.0;
            case OFFENSIVE_MIDFIELDER -> 0.5;
            case CENTRE_MIDFIELDER -> 0.35;
            case DEFENSIVE_MIDFIELDER -> 0.5;
            case RIGHT_WINGER -> 0.35;
            case RIGHT_MIDFIELDER -> 0.35;
            case RIGHT_WINGBACK -> 0.75;
            case RIGHT_BACK -> 1.0;
            case SWEEPER -> 0.5;
            default -> 0.0;
        };
    }

    private static double centerForwardDefenderSupport(PlayerPosition position) {
        return switch (position) {
            case LEFT_WINGER -> 0.25;
            case LEFT_MIDFIELDER -> 0.25;
            case OFFENSIVE_MIDFIELDER -> 0.5;
            case FORWARD -> 0.75;
            case CENTRE_MIDFIELDER -> 0.5;
            case DEFENSIVE_MIDFIELDER -> 0.75;
            case RIGHT_WINGER -> 0.25;
            case RIGHT_MIDFIELDER -> 0.25;
            case SWEEPER -> 1.0;
            default -> 0.0;
        };
    }

    private static double flankMidfieldDefenderSupport(PlayerPosition position) {
        return switch (position) {
            case LEFT_WINGER -> 0.75;
            case LEFT_MIDFIELDER -> 1.0;
            case LEFT_WINGBACK -> 1.0;
            case LEFT_BACK -> 1.0;
            case OFFENSIVE_MIDFIELDER -> 0.35;
            case CENTRE_MIDFIELDER -> 0.5;
            case DEFENSIVE_MIDFIELDER -> 0.75;
            case RIGHT_WINGER -> 0.75;
            case RIGHT_MIDFIELDER -> 1.0;
            case RIGHT_WINGBACK -> 1.0;
            case RIGHT_BACK -> 1.0;
            default -> 0.0;
        };
    }

    private static double centerMidfieldDefenderSupport(PlayerPosition position) {
        return switch (position) {
            case LEFT_WINGER -> 0.35;
            case LEFT_MIDFIELDER -> 0.5;
            case OFFENSIVE_MIDFIELDER -> 0.5;
            case FORWARD -> 0.25;
            case CENTRE_MIDFIELDER -> 0.75;
            case DEFENSIVE_MIDFIELDER -> 1.25;
            case RIGHT_WINGER -> 0.35;
            case RIGHT_MIDFIELDER -> 0.5;
            case SWEEPER -> 0.25;
            default -> 0.0;
        };
    }

    private static TeamState getTeam(GameState state, DuelRole role) {
        return switch (role) {
            case INITIATOR -> state.attackingTeam();
            case CHALLENGER -> state.defendingTeam();
        };
    }
}
