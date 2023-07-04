package com.kjeldsen.match.domain.event;

import com.kjeldsen.match.domain.aggregate.Player;
import com.kjeldsen.match.domain.id.DuelId;
import com.kjeldsen.match.domain.type.DuelModifier;
import com.kjeldsen.match.domain.type.DuelType;
import com.kjeldsen.match.domain.type.ModifierMetaInfo;
import com.kjeldsen.match.domain.type.PitchArea;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class DuelStartedEvent extends Event {

    private DuelId duelId;
    private Player attacker;
    private Player defender;
    private PitchArea pitchArea;
    private DuelType duelType;
    private List<ModifierMetaInfo<DuelModifier>> modifiers;
}
