package com.kjeldsen.match.entities;

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

    // Returns true if the area of a player from the *opposing* team is adjacent to or is the same
    // as this area. The "area" here is from the perspective of the defender, so "left" means the
    // defender's left, which is the opponent's right. Hence, if they are both on the "left" they
    // are on opposite sides of the pitch.
    public boolean opponentIsNearby(PitchArea area) {
        boolean leftAndRight = this.file() == PitchFile.LEFT && area.file() == PitchFile.LEFT;
        boolean rightAndLeft = this.file() == PitchFile.RIGHT && area.file() == PitchFile.RIGHT;
        boolean frontAndBack = this.rank() == PitchRank.FORWARD && area.rank() == PitchRank.FORWARD;
        boolean backAndFront = this.rank() == PitchRank.BACK && area.rank() == PitchRank.BACK;
        return !(leftAndRight || rightAndLeft || frontAndBack || backAndFront);
    }
}
