package com.kjeldsen.player.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public enum PitchArea {

    /*
     * The pitch is divided into a 3x3 grid. "Forward" and "Back" are relative to the attacking
     * team. Center forward maps directly onto the penalty box despite the area being larger.
     *
     * |----------------|------------------|-----------------|
     * |  LEFT_FORWARD  |  CENTRE_FORWARD  |  RIGHT_FORWARD  |
     * |----------------|------------------|-----------------|
     * |  LEFT_MIDFIELD |  CENTRE_MIDFIELD |  RIGHT_MIDFIELD |
     * |----------------|------------------|-----------------|
     * |  LEFT_BACK     |  CENTRE_BACK     |  RIGHT_BACK     |
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
     * |  LEFT          |  CENTRE          |  RIGHT          |
     * |----------------|------------------|-----------------|
     * |  LEFT          |  CENTRE          |  RIGHT          |
     * |----------------|------------------|-----------------|
     * |  LEFT          |  CENTRE          |  RIGHT          |
     * |----------------|------------------|-----------------|
     */

    LEFT_FORWARD,
    CENTRE_FORWARD, // Penalty box
    RIGHT_FORWARD,
    LEFT_MIDFIELD,
    CENTRE_MIDFIELD,
    RIGHT_MIDFIELD,
    LEFT_BACK,
    CENTRE_BACK,
    RIGHT_BACK,
    OUT_OF_BOUNDS;

    public enum PitchRank {
        FORWARD,
        MIDDLE,
        BACK
    }

    public enum PitchFile {
        LEFT,
        CENTRE,
        RIGHT
    }

    public PitchRank rank() {
        return switch (this) {
            case LEFT_FORWARD, CENTRE_FORWARD, RIGHT_FORWARD -> PitchRank.FORWARD;
            case LEFT_MIDFIELD, CENTRE_MIDFIELD, RIGHT_MIDFIELD -> PitchRank.MIDDLE;
            case LEFT_BACK, CENTRE_BACK, RIGHT_BACK -> PitchRank.BACK;
            default -> throw new RuntimeException("Out of bounds don't have pitch rank!");
        };
    }

    public PitchFile file() {
        return switch (this) {
            case LEFT_FORWARD, LEFT_MIDFIELD, LEFT_BACK -> PitchFile.LEFT;
            case CENTRE_FORWARD, CENTRE_MIDFIELD, CENTRE_BACK -> PitchFile.CENTRE;
            case RIGHT_FORWARD, RIGHT_MIDFIELD, RIGHT_BACK -> PitchFile.RIGHT;
            default -> throw new RuntimeException("Out of bounds don't have pitch file!");
        };
    }

    // Returns true if the area of a player from the *same* team is adjacent to or is the same as
    // this area.
    public boolean isNearby(PitchArea area) {
        boolean leftAndRight = this.file() == PitchFile.LEFT && area.file() == PitchFile.RIGHT;
        boolean rightAndLeft = this.file() == PitchFile.RIGHT && area.file() == PitchFile.LEFT;
        boolean frontAndBack = this.rank() == PitchRank.FORWARD && area.rank() == PitchRank.BACK;
        boolean backAndFront = this.rank() == PitchRank.BACK && area.rank() == PitchRank.FORWARD;
        return !(leftAndRight || rightAndLeft || frontAndBack || backAndFront);
    }

    public List<PitchArea> getNearbyAreas() {
        PitchFile pitchFile = this.file();
        PitchRank pitchRank = this.rank();

        log.info("Pitch file = {} rank = {}", pitchFile, pitchRank);

        return switch (pitchFile) {
            case CENTRE -> switch (pitchRank) {
                case FORWARD -> List.of(PitchArea.LEFT_FORWARD, PitchArea.RIGHT_FORWARD);
                case MIDDLE -> List.of(PitchArea.LEFT_MIDFIELD, PitchArea.RIGHT_MIDFIELD);
                case BACK   -> List.of(PitchArea.LEFT_BACK, PitchArea.RIGHT_BACK);
            };
            case LEFT, RIGHT -> switch (pitchRank) {
                case FORWARD -> List.of(PitchArea.CENTRE_FORWARD, PitchArea.OUT_OF_BOUNDS);
                case MIDDLE -> List.of(PitchArea.CENTRE_MIDFIELD, PitchArea.OUT_OF_BOUNDS);
                case BACK   -> List.of(PitchArea.CENTRE_BACK, PitchArea.OUT_OF_BOUNDS);
            };
        };
    }

    // Switches the perspective of the pitch area from the perspective of the attacking team to the
    // defending team and vice versa.
    public PitchArea flipPerspective() {
        return switch (this) {
            case LEFT_FORWARD -> RIGHT_BACK;
            case CENTRE_FORWARD -> CENTRE_BACK;
            case RIGHT_FORWARD -> LEFT_BACK;
            case LEFT_MIDFIELD -> RIGHT_MIDFIELD;
            case CENTRE_MIDFIELD -> CENTRE_MIDFIELD;
            case RIGHT_MIDFIELD -> LEFT_MIDFIELD;
            case LEFT_BACK -> RIGHT_FORWARD;
            case CENTRE_BACK -> CENTRE_FORWARD;
            case RIGHT_BACK -> LEFT_FORWARD;
            default -> throw new RuntimeException("Out of bounds don't have pitch rank");
        };
    }

    public PitchArea switchFile() {
        return switch (this) {
            case LEFT_FORWARD -> RIGHT_FORWARD;
            case CENTRE_FORWARD -> CENTRE_FORWARD;
            case RIGHT_FORWARD -> LEFT_FORWARD;
            case LEFT_MIDFIELD -> RIGHT_MIDFIELD;
            case CENTRE_MIDFIELD -> CENTRE_MIDFIELD;
            case RIGHT_MIDFIELD -> LEFT_MIDFIELD;
            case LEFT_BACK -> RIGHT_BACK;
            case CENTRE_BACK -> CENTRE_BACK;
            case RIGHT_BACK -> LEFT_BACK;
            default -> throw new RuntimeException("Out of bounds don't have pitch rank");
        };
    }

    public PitchArea rankUp() {
        return switch (this) {
            case LEFT_FORWARD -> LEFT_FORWARD;
            case CENTRE_FORWARD -> CENTRE_FORWARD;
            case RIGHT_FORWARD -> RIGHT_FORWARD;
            case LEFT_MIDFIELD -> LEFT_FORWARD;
            case CENTRE_MIDFIELD -> CENTRE_FORWARD;
            case RIGHT_MIDFIELD -> RIGHT_FORWARD;
            case LEFT_BACK -> LEFT_MIDFIELD;
            case CENTRE_BACK -> CENTRE_MIDFIELD;
            case RIGHT_BACK -> RIGHT_MIDFIELD;
            default -> throw new RuntimeException("Out of bounds don't have pitch rank");
        };
    }
    public static PitchArea[] midfield() {
        return new PitchArea[]{LEFT_MIDFIELD, CENTRE_MIDFIELD, RIGHT_MIDFIELD};
    }

    public static PitchArea[] midfieldAndForward() {
        return new PitchArea[]{LEFT_MIDFIELD, CENTRE_MIDFIELD, RIGHT_MIDFIELD, RIGHT_FORWARD, CENTRE_FORWARD, LEFT_FORWARD};
    }

}
