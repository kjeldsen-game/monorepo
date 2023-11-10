package com.kjeldsen.match.engine.modifers;

import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.engine.entities.PitchArea.PitchRank;

public enum VerticalPressure {

    /*
     * The vertical pressure modifier affects defensive assistance based on pitch ranks
     */

    MID_PRESSURE,
    LOW_PRESSURE,
    NO_VERTICAL_FOCUS;

    public double defensiveAssistanceBonus(PitchArea area) {
        return switch (this) {
            case MID_PRESSURE:
                if (area.rank() == PitchRank.MIDDLE) {
                    yield 0.1;
                }
                if (area.rank() == PitchRank.FORWARD) {
                    yield -0.1;
                }
                yield 0.0;
            case LOW_PRESSURE:
                if (area.rank() == PitchRank.FORWARD) {
                    yield 0.1;
                }
                if (area.rank() == PitchRank.MIDDLE) {
                    yield -0.1;
                }
                yield 0.0;
            case NO_VERTICAL_FOCUS:
                yield 0.0;
        };
    }
}