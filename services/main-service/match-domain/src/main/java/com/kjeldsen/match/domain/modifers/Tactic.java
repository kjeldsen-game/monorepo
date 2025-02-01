package com.kjeldsen.match.domain.modifers;

import com.kjeldsen.match.domain.entities.duel.DuelRole;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerPosition;

public enum Tactic {

    /*
     * The tactic modifier affects both offensive and defensive assistance, and player selection.
     * The return values of these methods should be multiplied by the base value i.e. a factor of
     *  2 means a 100% increase.
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


    // Tactics affecting player selection return a factor that increase or decrease the probability
    // of a player being selected.
    public double selectionFactor(PlayerPosition playerPosition) {
        return switch (this) {
            case TIKA_TAKA -> {
                if (playerPosition.isCentral()) {
                    yield 2.0;
                }
                yield 1.0;
            }
            case WING_PLAY -> {
                if (!playerPosition.isCentral()) {
                    yield 2.0;
                }
                yield 1.0;
            }
            default -> 1.0; // Tactics that do not affect selection
        };
    }

    // Tactics that affect assistance return a modifier percentage increase or decrease
    public double assistanceFactor(PlayerPosition position, DuelRole role, PitchArea pitchArea) {
        return switch (this) {
            case DOUBLE_TEAM -> doubleTeam(role, pitchArea);
            case MAN_ON_MAN -> manOnMan(role, pitchArea);
            case ZONE -> zone(role, pitchArea);
            case COUNTER_ATTACK -> counterAttack(role);
            case POSSESSION_CONTROL -> possessionControl(position, role, pitchArea);
            case CATENACCIO -> catenaccio(role, pitchArea);
            case ROUTE_ONE -> routeOne(role, pitchArea);
            case OFFSIDE_TRAP -> offsideTrap(role, pitchArea);
            default -> 1.0; // Tactics that do not affect assistance
        };
    }

    private double doubleTeam(DuelRole role, PitchArea area) {
        // TODO incomplete
        return 1.0;
    }

    private double manOnMan(DuelRole role, PitchArea area) {
        // TODO incomplete
        return switch (role) {
            case CHALLENGER -> {
                if (area == PitchArea.CENTRE_BACK) {
                    yield 0.85;
                }
                yield 0.0;
            }
            case INITIATOR -> {
                if (area == PitchArea.CENTRE_FORWARD) {
                    yield 0.85;
                }
                yield 0.0;
            }
        };
    }

    private double zone(DuelRole role, PitchArea area) {
        // TODO incomplete
        return switch (role) {
            case CHALLENGER -> {
                if (area == PitchArea.CENTRE_BACK) {
                    yield 1.15;
                }
                yield 1.0;
            }
            case INITIATOR -> {
                if (area == PitchArea.CENTRE_FORWARD) {
                    yield 1.15;
                }
                yield 1.0;
            }
        };
    }


    private double counterAttack(DuelRole role) {
        return switch (role) {
            case CHALLENGER -> 1.2;
            case INITIATOR -> 0.9;
        };
    }

    private double possessionControl(PlayerPosition position, DuelRole role, PitchArea area) {
        if (position.isMidfielder() && position.isNatural()) {
            return 1.05;
        }
        if (position.isMidfielder() && position.isOffensive()) {
            return 1.03;
        }
        return 1.0;
    }

    private double catenaccio(DuelRole role, PitchArea area) {
        return 1.0;
    }

    private double routeOne(DuelRole role, PitchArea area) {
        return 1.0;
    }

    private double offsideTrap(DuelRole role, PitchArea area) {
        return 1.0;
    }
}
