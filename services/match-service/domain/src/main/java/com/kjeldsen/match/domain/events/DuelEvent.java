package com.kjeldsen.match.domain.events;

import com.kjeldsen.match.domain.aggregate.PitchArea;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@SuperBuilder
public class DuelEvent extends BaseEvent {

    private String playId;
    private String attackerId;
    private String defenderId;
    private Map<String, String> attackerModifiers;
    private Map<String, String> defenderModifiers;
    private PitchArea pitchArea;

}
