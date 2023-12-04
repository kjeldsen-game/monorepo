package com.kjeldsen.match.engine.modifers;

import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PitchArea.PitchFile;

public enum HorizontalPressure {

    /*
     * The horizontal pressure modifier affects defensive assistance based on pitch files
     */

    SWARM_CENTRE,
    SWARM_FLANKS,
    NO_HORIZONTAL_FOCUS;

    public double defensiveAssistanceModifier(PitchArea area) {
        return switch (this) {
            case SWARM_CENTRE:
                if (area.file() == PitchFile.CENTRE) {
                    yield 1.15;
                }
                yield 0.85;
            case SWARM_FLANKS:
                if (area.file() != PitchFile.CENTRE) {
                    yield 1.15;
                }
                yield 0.85;
            case NO_HORIZONTAL_FOCUS:
                yield 1.0;
        };
    }
}
