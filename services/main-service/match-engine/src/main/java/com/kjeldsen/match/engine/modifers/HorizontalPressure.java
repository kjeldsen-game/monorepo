package com.kjeldsen.match.engine.modifers;

import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.engine.entities.PitchArea.PitchFile;

public enum HorizontalPressure {

    /*
     * The horizontal pressure modifier affects defensive assistance based on pitch files
     */

    SWARM_CENTER,
    SWARM_FLANKS,
    NO_HORIZONTAL_FOCUS;

    public double defensiveAssistanceBonus(PitchArea area) {
        return switch (this) {
            case SWARM_CENTER:
                if (area.file() == PitchFile.CENTER) {
                    yield 0.15;
                }
                yield -0.15;
            case SWARM_FLANKS:
                if (area.file() != PitchFile.CENTER) {
                    yield 0.15;
                }
                yield -0.15;
            case NO_HORIZONTAL_FOCUS:
                yield 0.0;
        };
    }
}
