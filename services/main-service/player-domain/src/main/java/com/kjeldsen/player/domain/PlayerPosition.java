package com.kjeldsen.player.domain;

import java.util.List;
import lombok.Getter;

@Getter
public enum PlayerPosition {
    CENTRE_BACK,
    AERIAL_CENTRE_BACK,
    SWEEPER,
    LEFT_BACK,
    RIGHT_BACK,
    LEFT_WINGBACK,
    RIGHT_WINGBACK,
    DEFENSIVE_MIDFIELDER,
    CENTRE_MIDFIELDER,
    LEFT_MIDFIELDER,
    RIGHT_MIDFIELDER,
    LEFT_WINGER,
    OFFENSIVE_MIDFIELDER,
    RIGHT_WINGER,
    FORWARD,
    AERIAL_FORWARD,
    STRIKER,
    AERIAL_STRIKER,
    GOALKEEPER;

    public List<PlayerPosition> forwardList() {
        return List.of(
            PlayerPosition.FORWARD,
            PlayerPosition.AERIAL_FORWARD,
            PlayerPosition.STRIKER,
            PlayerPosition.AERIAL_FORWARD);
    }

    public List<PlayerPosition> midfielderList() {
        return List.of(
            PlayerPosition.LEFT_MIDFIELDER,
            PlayerPosition.RIGHT_MIDFIELDER,
            PlayerPosition.CENTRE_MIDFIELDER,
            PlayerPosition.DEFENSIVE_MIDFIELDER,
            PlayerPosition.OFFENSIVE_MIDFIELDER,
            PlayerPosition.LEFT_WINGER,
            PlayerPosition.RIGHT_WINGER,
            PlayerPosition.LEFT_WINGBACK,
            PlayerPosition.RIGHT_WINGBACK);
    }

    public List<PlayerPosition> defenderList() {
        return List.of(
            PlayerPosition.SWEEPER,
            PlayerPosition.LEFT_BACK,
            PlayerPosition.RIGHT_BACK,
            PlayerPosition.CENTRE_BACK);
    }

    public boolean isForward() {
        return forwardList().contains(this);
    }

    public boolean isMidfielder() {
        return midfielderList().contains(this);
    }

    public boolean isDefender() {
        return defenderList().contains(this);
    }

    public boolean isCentral() {
        return List.of(
                PlayerPosition.CENTRE_BACK,
                PlayerPosition.AERIAL_CENTRE_BACK,
                PlayerPosition.CENTRE_MIDFIELDER,
                PlayerPosition.OFFENSIVE_MIDFIELDER,
                PlayerPosition.DEFENSIVE_MIDFIELDER,
                PlayerPosition.SWEEPER,
                PlayerPosition.AERIAL_STRIKER,
                PlayerPosition.STRIKER)
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
                PlayerPosition.CENTRE_BACK,
                PlayerPosition.LEFT_MIDFIELDER,
                PlayerPosition.RIGHT_MIDFIELDER,
                PlayerPosition.CENTRE_MIDFIELDER,
                PlayerPosition.FORWARD,
                PlayerPosition.AERIAL_FORWARD,
                PlayerPosition.STRIKER,
                PlayerPosition.AERIAL_STRIKER)
            .contains(this);
    }

    public boolean isOffensive() {
        return List.of(
                PlayerPosition.SWEEPER,
                PlayerPosition.LEFT_WINGER,
                PlayerPosition.RIGHT_WINGER,
                PlayerPosition.OFFENSIVE_MIDFIELDER)
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
                PlayerPosition.AERIAL_CENTRE_BACK,
                PlayerPosition.CENTRE_BACK,
                PlayerPosition.RIGHT_BACK)
            .contains(this);
    }

    // Pitch areas covered by the player - where the player is expected to be given the position.
    public List<PitchArea> coverage() {
        return switch (this) {
            case GOALKEEPER, SWEEPER, CENTRE_BACK, AERIAL_CENTRE_BACK ->
                List.of(PitchArea.CENTRE_BACK);
            case RIGHT_BACK -> List.of(PitchArea.RIGHT_BACK);
            case LEFT_BACK -> List.of(PitchArea.LEFT_BACK);
            case RIGHT_WINGBACK ->
                List.of(PitchArea.RIGHT_BACK, PitchArea.RIGHT_MIDFIELD, PitchArea.RIGHT_FORWARD);
            case LEFT_WINGBACK ->
                List.of(PitchArea.LEFT_BACK, PitchArea.LEFT_MIDFIELD, PitchArea.LEFT_FORWARD);
            case DEFENSIVE_MIDFIELDER -> List.of(PitchArea.CENTRE_BACK, PitchArea.CENTRE_MIDFIELD);
            case CENTRE_MIDFIELDER -> List.of(PitchArea.CENTRE_MIDFIELD);
            case OFFENSIVE_MIDFIELDER ->
                List.of(PitchArea.CENTRE_MIDFIELD, PitchArea.CENTRE_FORWARD);
            case RIGHT_MIDFIELDER -> List.of(PitchArea.RIGHT_MIDFIELD, PitchArea.RIGHT_FORWARD);
            case LEFT_MIDFIELDER -> List.of(PitchArea.LEFT_MIDFIELD, PitchArea.LEFT_FORWARD);
            case RIGHT_WINGER -> List.of(PitchArea.RIGHT_MIDFIELD, PitchArea.RIGHT_FORWARD);
            case LEFT_WINGER -> List.of(PitchArea.LEFT_MIDFIELD, PitchArea.LEFT_FORWARD);
            case FORWARD, AERIAL_FORWARD ->
                List.of(PitchArea.LEFT_FORWARD, PitchArea.CENTRE_FORWARD, PitchArea.RIGHT_FORWARD);
            case STRIKER, AERIAL_STRIKER -> List.of(PitchArea.CENTRE_FORWARD);
        };
    }
}
