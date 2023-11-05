package com.kjeldsen.match.engine.execution;

import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.engine.entities.PitchArea.PitchRank;
import com.kjeldsen.match.engine.entities.PlayerPosition;
import com.kjeldsen.match.engine.entities.SkillType;
import com.kjeldsen.match.engine.entities.duel.DuelRole;
import com.kjeldsen.match.engine.modifers.Tactic;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.engine.state.TeamState;
import com.kjeldsen.match.models.Player;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Assistance {

    /*
     * Determines the assistance a player gets from his teammates in a positional duel.
     */

    public static final int MAX_ASSISTANCE = 25;
    public static final int MIN_ASSISTANCE = -25;
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

        return switch (role) {
            case INITIATOR -> {
                double factor = attackerSupport(player.getPosition(), ballArea);
                double op = player.getSkills().get(SkillType.OFFENSIVE_POSITIONING);
                double bc =
                    player.getSkills().get(SkillType.BALL_CONTROL) * BALL_CONTROL_FACTOR;
                double modifier =
                    tactic.assistanceBonus(player.getPosition(), DuelRole.INITIATOR, ballArea);
                yield (int) ((factor * (op + bc)) * (1 + modifier));
            }

            case CHALLENGER -> {
                double factor = defenderSupport(player.getPosition(), ballArea);
                double dp = player.getSkills().get(SkillType.DEFENSIVE_POSITIONING);
                double ta = player.getSkills().get(SkillType.TACKLING) * TACKLING_FACTOR;
                yield (int) (factor * (dp + ta));
            }
        };
    }

    // Takes the assistance that two players in a duel receive and normalizes the difference to a
    // value between constants defined in this class.
    public static Map<DuelRole, Integer> normalizeAssistance(
        Map<String, Integer> initiatorAssistance, Map<String, Integer> challengerAssistance) {

        int initiatorTotal =
            initiatorAssistance.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        int challengerTotal = challengerAssistance.values().stream()
            .mapToInt(Integer::intValue)
            .sum();

        int total = initiatorTotal + challengerTotal;
        if (total == 0) {
            return Map.of(
                DuelRole.INITIATOR, 0,
                DuelRole.CHALLENGER, 0);
        }

        // Normalized point difference is given to winner
        DuelRole winner =
            initiatorTotal > challengerTotal ? DuelRole.INITIATOR : DuelRole.CHALLENGER;

        return switch (winner) {
            case INITIATOR -> setAssistance(DuelRole.INITIATOR, initiatorTotal, total);
            case CHALLENGER -> setAssistance(DuelRole.CHALLENGER, challengerTotal, total);
        };
    }

    // Given the winner's total, returns a value between the min and max assistance constants
    private static Map<DuelRole, Integer> setAssistance(
        DuelRole winner, int winnerTotal, int total) {
        double factor = (double) winnerTotal / total;
        int assistance =
            (int) (Math.abs((MAX_ASSISTANCE - MIN_ASSISTANCE)) * factor) + MIN_ASSISTANCE;

        DuelRole loser = winner == DuelRole.INITIATOR ? DuelRole.CHALLENGER : DuelRole.INITIATOR;
        return Map.of(
            winner, assistance,
            loser, 0);
    }

    // Factor by which assistance is multiplied by for the attacking player
    private static double attackerSupport(PlayerPosition position, PitchArea area) {
        if (area.rank() == PitchRank.FORWARD) {
            return switch (area.file()) {
                case LEFT, RIGHT -> flankForwardAttackerSupport(position);
                case CENTER -> centerForwardAttackerSupport(position);
            };
        }
        if (area.rank() == PitchRank.MIDDLE) {
            return switch (area.file()) {
                case LEFT, RIGHT -> flankMidfieldAttackerSupport(position);
                case CENTER -> centerMidfieldAttackerSupport(position);
            };
        }

        return 0.0;
    }

    // Factor by which assistance is multiplied by for the defending player
    private static double defenderSupport(PlayerPosition position, PitchArea area) {
        if (area.rank() == PitchRank.FORWARD) {
            return switch (area.file()) {
                case LEFT, RIGHT -> flankForwardDefenderSupport(position);
                case CENTER -> centerForwardDefenderSupport(position);
            };
        }
        if (area.rank() == PitchRank.MIDDLE) {
            return switch (area.file()) {
                case LEFT, RIGHT -> flankMidfieldDefenderSupport(position);
                case CENTER -> centerMidfieldDefenderSupport(position);
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
            case CENTER_MIDFIELDER -> 0.35;
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
            case CENTER_MIDFIELDER -> 0.5;
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
            case CENTER_MIDFIELDER -> 0.5;
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
            case CENTER_MIDFIELDER -> 1.0;
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
            case CENTER_MIDFIELDER -> 0.35;
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
            case CENTER_MIDFIELDER -> 0.5;
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
            case CENTER_MIDFIELDER -> 0.5;
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
            case CENTER_MIDFIELDER -> 0.75;
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
