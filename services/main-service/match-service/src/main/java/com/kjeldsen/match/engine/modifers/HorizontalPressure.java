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

    public double defensiveAssistanceModifier(PitchArea area) {
        return switch (this) {
            case SWARM_CENTER:
                if (area.file() == PitchFile.CENTER) {
                    yield 1.15;
                }
                yield 0.85;
            case SWARM_FLANKS:
                if (area.file() != PitchFile.CENTER) {
                    yield 1.15;
                }
                yield 0.85;
            case NO_HORIZONTAL_FOCUS:
                yield 1.0;
        };
    }
}
