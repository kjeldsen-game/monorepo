package com.kjeldsen.match.engine.entities;

import java.util.List;

public enum PlayerPosition {

    /*
     * Exhaustive list of player positions.
     */

    GOALKEEPER,
    SWEEPER,
    CENTER_BACK, // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
    RIGHT_BACK,
    LEFT_BACK,
    RIGHT_WINGBACK,
    LEFT_WINGBACK,
    DEFENSIVE_MIDFIELDER,
    CENTER_MIDFIELDER, // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
    OFFENSIVE_MIDFIELDER,
    RIGHT_MIDFIELDER,
    LEFT_MIDFIELDER,
    RIGHT_WINGER,
    LEFT_WINGER,
    FALSE_NINE, //TODO NADIR MAYBE THIS IS AN ERROR OR AN OLD POSITION BECAUSE I DON'T REMEMBER THIS POSITION AT ALL IN DOCUMENTATION
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
                PlayerPosition.CENTER_MIDFIELDER, // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
                PlayerPosition.DEFENSIVE_MIDFIELDER,
                PlayerPosition.OFFENSIVE_MIDFIELDER,
                PlayerPosition.LEFT_WINGER,
                PlayerPosition.RIGHT_WINGER,
                PlayerPosition.LEFT_WINGBACK,
                PlayerPosition.RIGHT_WINGBACK,
                PlayerPosition.FALSE_NINE) //TODO NADIR MAYBE THIS IS AN ERROR OR AN OLD POSITION BECAUSE I DON'T REMEMBER THIS POSITION AT ALL IN DOCUMENTATION
            .contains(this);
    }

    public boolean isDefender() {
        return List.of(
                PlayerPosition.SWEEPER,
                PlayerPosition.LEFT_BACK,
                PlayerPosition.RIGHT_BACK,
                PlayerPosition.CENTER_BACK) // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
            .contains(this);
    }

    public boolean isCentral() {
        return List.of(
                PlayerPosition.CENTER_BACK, // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
                PlayerPosition.CENTER_MIDFIELDER, // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
                PlayerPosition.OFFENSIVE_MIDFIELDER,
                PlayerPosition.DEFENSIVE_MIDFIELDER,
                PlayerPosition.SWEEPER,
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
                PlayerPosition.CENTER_BACK, // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
                PlayerPosition.LEFT_MIDFIELDER,
                PlayerPosition.RIGHT_MIDFIELDER,
                PlayerPosition.CENTER_MIDFIELDER, // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
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
                PlayerPosition.FALSE_NINE) //TODO NADIR MAYBE THIS IS AN ERROR OR AN OLD POSITION BECAUSE I DON'T REMEMBER THIS POSITION AT ALL IN DOCUMENTATION
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

    // Pitch areas covered by the player - where the player is expected to be given the position.
    public List<PitchArea> coverage() {
        return switch (this) {
            case GOALKEEPER, SWEEPER, CENTER_BACK -> List.of(PitchArea.CENTER_BACK); // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
            case RIGHT_BACK -> List.of(PitchArea.RIGHT_BACK);
            case LEFT_BACK -> List.of(PitchArea.LEFT_BACK);
            case RIGHT_WINGBACK ->
                List.of(PitchArea.RIGHT_BACK, PitchArea.RIGHT_MIDFIELD, PitchArea.RIGHT_FORWARD);
            case LEFT_WINGBACK ->
                List.of(PitchArea.LEFT_BACK, PitchArea.LEFT_MIDFIELD, PitchArea.LEFT_FORWARD);
            case DEFENSIVE_MIDFIELDER -> List.of(PitchArea.CENTER_BACK, PitchArea.CENTER_MIDFIELD); // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
            case CENTER_MIDFIELDER -> List.of(PitchArea.CENTER_MIDFIELD); // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
            case OFFENSIVE_MIDFIELDER, FALSE_NINE -> //TODO NADIR MAYBE THIS IS AN ERROR OR AN OLD POSITION BECAUSE I DON'T REMEMBER THIS POSITION AT ALL IN DOCUMENTATION
                List.of(PitchArea.CENTER_MIDFIELD, PitchArea.CENTER_FORWARD); // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
            case RIGHT_MIDFIELDER -> List.of(PitchArea.RIGHT_MIDFIELD, PitchArea.RIGHT_FORWARD);
            case LEFT_MIDFIELDER -> List.of(PitchArea.LEFT_MIDFIELD, PitchArea.LEFT_FORWARD);
            case RIGHT_WINGER -> List.of(PitchArea.RIGHT_MIDFIELD, PitchArea.RIGHT_FORWARD,
                PitchArea.CENTER_FORWARD); // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
            case LEFT_WINGER ->
                List.of(PitchArea.LEFT_MIDFIELD, PitchArea.LEFT_FORWARD, PitchArea.CENTER_FORWARD); // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
            case FORWARD ->
                List.of(PitchArea.LEFT_FORWARD, PitchArea.CENTER_FORWARD, PitchArea.RIGHT_FORWARD); // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
            case STRIKER -> List.of(PitchArea.CENTER_FORWARD); // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
        };
    }
}
