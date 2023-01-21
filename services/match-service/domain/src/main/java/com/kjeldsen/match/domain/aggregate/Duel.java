package com.kjeldsen.match.domain.aggregate;

import com.kjeldsen.match.domain.type.DuelResult;
import com.kjeldsen.match.domain.type.DuelType;
import com.kjeldsen.match.domain.type.PitchArea;

public class Duel {

    private Player attacker;
    private Player defender;
    private DuelType type;
    private PitchArea pitchArea;
    // Always from the attacker point of view
    private DuelResult result;

}
