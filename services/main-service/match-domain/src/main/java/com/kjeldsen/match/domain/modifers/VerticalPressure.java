package com.kjeldsen.match.domain.modifers;


import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PitchArea.PitchRank;

public enum VerticalPressure {

    /*
     * The vertical pressure modifier affects defensive assistance based on pitch ranks
     */

    MID_PRESSURE,
    LOW_PRESSURE,
    NO_VERTICAL_FOCUS;

    public double defensiveAssistanceModifier(PitchArea area) {
        return switch (this) {
            case MID_PRESSURE:
                if (area.rank() == PitchRank.MIDDLE) {
                    yield 1.1;
                }
                if (area.rank() == PitchRank.FORWARD) {
                    yield 0.9;
                }
                yield 1.0;
            case LOW_PRESSURE:
                if (area.rank() == PitchRank.FORWARD) {
                    yield 1.1;
                }
                if (area.rank() == PitchRank.MIDDLE) {
                    yield 0.9;
                }
                yield 1.0;
            case NO_VERTICAL_FOCUS:
                yield 1.0;
        };
    }
}
