package com.kjeldsen.match.domain.type;

import java.util.List;

public enum PlayerPosition {

    /*
     * Exhaustive list of player positions.
     */

    GOALKEEPER,
    SWEEPER,
    CENTER_BACK,
    RIGHT_BACK,
    LEFT_BACK,
    RIGHT_WINGBACK,
    LEFT_WINGBACK,
    DEFENSIVE_MIDFIELDER,
    CENTER_MIDFIELDER,
    OFFENSIVE_MIDFIELDER,
    RIGHT_MIDFIELDER,
    LEFT_MIDFIELDER,
    RIGHT_WINGER,
    LEFT_WINGER,
    FALSE_NINE,
    FORWARD,
    STRIKER;

    public boolean isForward() {
        return List.of(
                PlayerPosition.FORWARD,
                PlayerPosition.STRIKER)
            .contains(this);
    }

    public boolean isMidfielder() {
        return List.of(
                PlayerPosition.LEFT_MIDFIELDER,
                PlayerPosition.RIGHT_MIDFIELDER,
                PlayerPosition.CENTER_MIDFIELDER,
                PlayerPosition.DEFENSIVE_MIDFIELDER,
                PlayerPosition.OFFENSIVE_MIDFIELDER,
                PlayerPosition.LEFT_WINGER,
                PlayerPosition.RIGHT_WINGER,
                PlayerPosition.LEFT_WINGBACK,
                PlayerPosition.RIGHT_WINGBACK,
                PlayerPosition.FALSE_NINE)
            .contains(this);
    }

    public boolean isDefender() {
        return List.of(
                PlayerPosition.SWEEPER,
                PlayerPosition.LEFT_BACK,
                PlayerPosition.RIGHT_BACK,
                PlayerPosition.CENTER_BACK)
            .contains(this);
    }

    public boolean isCentral() {
        return List.of(
                PlayerPosition.CENTER_BACK,
                PlayerPosition.CENTER_MIDFIELDER,
                PlayerPosition.OFFENSIVE_MIDFIELDER,
                PlayerPosition.DEFENSIVE_MIDFIELDER,
                PlayerPosition.SWEEPER)
            .contains(this);
    }

    public boolean isWingback() {
        return List.of(
                PlayerPosition.LEFT_WINGBACK,
                PlayerPosition.RIGHT_WINGBACK)
            .contains(this);
    }

    public boolean isNatural() {
        return List.of(
                PlayerPosition.LEFT_BACK,
                PlayerPosition.RIGHT_BACK,
                PlayerPosition.CENTER_BACK,
                PlayerPosition.LEFT_MIDFIELDER,
                PlayerPosition.RIGHT_MIDFIELDER,
                PlayerPosition.CENTER_MIDFIELDER,
                PlayerPosition.FORWARD,
                PlayerPosition.STRIKER)
            .contains(this);
    }

    public boolean isOffensive() {
        return List.of(
                PlayerPosition.SWEEPER,
                PlayerPosition.LEFT_WINGER,
                PlayerPosition.RIGHT_WINGER,
                PlayerPosition.OFFENSIVE_MIDFIELDER,
                PlayerPosition.FALSE_NINE)
            .contains(this);
    }

    public boolean isDefensive() {
        return List.of(
                PlayerPosition.RIGHT_WINGBACK,
                PlayerPosition.LEFT_WINGBACK,
                PlayerPosition.DEFENSIVE_MIDFIELDER)
            .contains(this);
    }

    public boolean isFullBack() {
        return List.of(
                PlayerPosition.LEFT_BACK,
                PlayerPosition.CENTER_BACK,
                PlayerPosition.RIGHT_BACK)
            .contains(this);
    }

}
