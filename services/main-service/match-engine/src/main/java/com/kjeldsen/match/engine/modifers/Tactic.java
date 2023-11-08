package com.kjeldsen.match.engine.modifers;

import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.engine.entities.PlayerPosition;
import com.kjeldsen.match.engine.entities.duel.DuelRole;

public enum Tactic {

    /*
     * The tactic modifier affects both offensive and defensive assistance, as well as player
     * selection.
     *
     * The selection and assistance bonuses are percentages (0.0 - 1.0) that are then summed when
     * calculating the total bonus. E.g. a player may receive a 0.1 (10%) increase from one modifier
     * and another 0.15 (15%) increase from another modifier, resulting in a 0.25 (25%) increase.
     * These should then be added to 1.0 to get the factor, 1.25 (125%), which can be then be used
     * to multiply the base value by.
     */

    DOUBLE_TEAM,
    MAN_ON_MAN,
    ZONE,
    COUNTER_ATTACK,
    POSSESSION_CONTROL,
    TIKA_TAKA,
    WING_PLAY,
    CATENACCIO,
    ROUTE_ONE,
    OFFSIDE_TRAP;


    // Tactics that affect player selection return a bonus percentage increase or decrease. A value
    // of 1.0 means the probability of selection is double (i.e. a 100% increase)
    public double selectionBonus(PlayerPosition playerPosition) {
        return switch (this) {
            case TIKA_TAKA -> {
                if (playerPosition.isCentral()) {
                    yield 1.0;
                }
                yield 0.0;
            }
            case WING_PLAY -> {
                if (!playerPosition.isCentral()) {
                    yield 1.0;
                }
                yield 0.0;
            }
            default -> 0.0; // Tactics that do not affect selection
        };
    }

    // Tactics that affect assistance return a bonus percentage increase or decrease
    public double assistanceBonus(PlayerPosition position, DuelRole role, PitchArea pitchArea) {
        return switch (this) {
            case DOUBLE_TEAM -> doubleTeam(role, pitchArea);
            case MAN_ON_MAN -> manOnMan(role, pitchArea);
            case ZONE -> zone(role, pitchArea);
            case COUNTER_ATTACK -> counterAttack(role);
            case POSSESSION_CONTROL -> possessionControl(position, role, pitchArea);
            case CATENACCIO -> catenaccio(role, pitchArea);
            case ROUTE_ONE -> routeOne(role, pitchArea);
            case OFFSIDE_TRAP -> offsideTrap(role, pitchArea);
            default -> 0.0; // Tactics that do not affect assistance
        };
    }

    private double doubleTeam(DuelRole role, PitchArea area) {
        // TODO incomplete
        return 0;
    }

    private double manOnMan(DuelRole role, PitchArea area) {
        // TODO incomplete
        return switch (role) {
            case CHALLENGER -> {
                if (area == PitchArea.CENTER_BACK) {
                    yield -0.15;
                }
                yield 0.0;
            }
            case INITIATOR -> {
                if (area == PitchArea.CENTER_FORWARD) {
                    yield -0.15;
                }
                yield 0.0;
            }
        };
    }

    private double zone(DuelRole role, PitchArea area) {
        // TODO incomplete
        return switch (role) {
            case CHALLENGER -> {
                if (area == PitchArea.CENTER_BACK) {
                    yield 0.15;
                }
                yield 0.0;
            }
            case INITIATOR -> {
                if (area == PitchArea.CENTER_FORWARD) {
                    yield 0.15;
                }
                yield 0.0;
            }
        };
    }


    private double counterAttack(DuelRole role) {
        return switch (role) {
            case CHALLENGER -> 0.2;
            case INITIATOR -> -0.1;
        };
    }

    private double possessionControl(PlayerPosition position, DuelRole role, PitchArea area) {
        if (position.isMidfielder() && position.isNatural()) {
            return 0.05;
        }
        if (position.isMidfielder() && position.isOffensive()) {
            return 0.03;
        }
        return 0.0;
    }

    private double catenaccio(DuelRole role, PitchArea area) {
        return 0;
    }

    private double routeOne(DuelRole role, PitchArea area) {
        return 0;
    }

    private double offsideTrap(DuelRole role, PitchArea area) {
        return 0;
    }
}
