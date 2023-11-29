package com.kjeldsen.match.engine.entities;

public enum PitchArea {

    /*
     * The pitch is divided into a 3x3 grid. "Forward" and "Back" are relative to the attacking
     * team. Center forward maps directly onto the penalty box despite the area being larger.
     *
     * |----------------|------------------|-----------------|
     * |  LEFT_FORWARD  |  CENTER_FORWARD  |  RIGHT_FORWARD  |
     * |----------------|------------------|-----------------|
     * |  LEFT_MIDFIELD |  CENTER_MIDFIELD |  RIGHT_MIDFIELD |
     * |----------------|------------------|-----------------|
     * |  LEFT_BACK     |  CENTER_BACK     |  RIGHT_BACK     |
     * |----------------|------------------|-----------------|
     *
     * The pitch is further categorised into forward, middle, and back ranks:
     *
     * |----------------|------------------|-----------------|
     * |  FORWARD       |  FORWARD         |  FORWARD        |
     * |----------------|------------------|-----------------|
     * |  MIDDLE        |  MIDDLE          |  MIDDLE         |
     * |----------------|------------------|-----------------|
     * |  BACK          |  BACK            |  BACK           |
     * |----------------|------------------|-----------------|
     *
     * And left, center, and right files:
     *
     * |----------------|------------------|-----------------|
     * |  LEFT          |  CENTER          |  RIGHT          |
     * |----------------|------------------|-----------------|
     * |  LEFT          |  CENTER          |  RIGHT          |
     * |----------------|------------------|-----------------|
     * |  LEFT          |  CENTER          |  RIGHT          |
     * |----------------|------------------|-----------------|
     */

    LEFT_FORWARD,
    CENTER_FORWARD, // Penalty box
    RIGHT_FORWARD,
    LEFT_MIDFIELD,
    CENTER_MIDFIELD,
    RIGHT_MIDFIELD,
    LEFT_BACK,
    CENTER_BACK,
    RIGHT_BACK;

    public enum PitchRank {
        FORWARD,
        MIDDLE,
        BACK
    }

    public enum PitchFile {
        LEFT,
        CENTER,
        RIGHT
    }

    public PitchRank rank() {
        return switch (this) {
            case LEFT_FORWARD, CENTER_FORWARD, RIGHT_FORWARD -> PitchRank.FORWARD;
            case LEFT_MIDFIELD, CENTER_MIDFIELD, RIGHT_MIDFIELD -> PitchRank.MIDDLE;
            case LEFT_BACK, CENTER_BACK, RIGHT_BACK -> PitchRank.BACK;
        };
    }

    public PitchFile file() {
        return switch (this) {
            case LEFT_FORWARD, LEFT_MIDFIELD, LEFT_BACK -> PitchFile.LEFT;
            case CENTER_FORWARD, CENTER_MIDFIELD, CENTER_BACK -> PitchFile.CENTER;
            case RIGHT_FORWARD, RIGHT_MIDFIELD, RIGHT_BACK -> PitchFile.RIGHT;
        };
    }

    // Returns true if the area of a player from the *same* team is adjacent to or is the same as
    // this area.
    public boolean teammateIsNearby(PitchArea area) {
        boolean leftAndRight = this.file() == PitchFile.LEFT && area.file() == PitchFile.RIGHT;
        boolean rightAndLeft = this.file() == PitchFile.RIGHT && area.file() == PitchFile.LEFT;
        boolean frontAndBack = this.rank() == PitchRank.FORWARD && area.rank() == PitchRank.BACK;
        boolean backAndFront = this.rank() == PitchRank.BACK && area.rank() == PitchRank.FORWARD;
        return !(leftAndRight || rightAndLeft || frontAndBack || backAndFront);
    }

    // Switches the perspective of the pitch area from the perspective of the attacking team to the
    // defending team and vice versa.
    public PitchArea flipPerspective() {
        return switch (this) {
            case LEFT_FORWARD -> RIGHT_BACK;
            case CENTER_FORWARD -> CENTER_BACK;
            case RIGHT_FORWARD -> LEFT_BACK;
            case LEFT_MIDFIELD -> RIGHT_MIDFIELD;
            case CENTER_MIDFIELD -> CENTER_MIDFIELD;
            case RIGHT_MIDFIELD -> LEFT_MIDFIELD;
            case LEFT_BACK -> RIGHT_FORWARD;
            case CENTER_BACK -> CENTER_FORWARD;
            case RIGHT_BACK -> LEFT_FORWARD;
        };
    }

    public PitchArea switchFile() {
        return switch (this) {
            case LEFT_FORWARD -> RIGHT_FORWARD;
            case CENTER_FORWARD -> CENTER_FORWARD;
            case RIGHT_FORWARD -> LEFT_FORWARD;
            case LEFT_MIDFIELD -> RIGHT_MIDFIELD;
            case CENTER_MIDFIELD -> CENTER_MIDFIELD;
            case RIGHT_MIDFIELD -> LEFT_MIDFIELD;
            case LEFT_BACK -> RIGHT_BACK;
            case CENTER_BACK -> CENTER_BACK;
            case RIGHT_BACK -> LEFT_BACK;
        };
    }
}
