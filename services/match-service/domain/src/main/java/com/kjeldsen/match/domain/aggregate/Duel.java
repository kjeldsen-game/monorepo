package com.kjeldsen.match.domain.aggregate;

import com.kjeldsen.match.domain.event.DuelStartedEvent;
import com.kjeldsen.match.domain.processor.DuelStrategy;
import com.kjeldsen.match.domain.processor.DuelStrategyFactory;
import com.kjeldsen.match.domain.type.DuelModifier;
import com.kjeldsen.match.domain.type.DuelResult;
import com.kjeldsen.match.domain.type.DuelType;
import com.kjeldsen.match.domain.type.ModifierMetaInfo;
import com.kjeldsen.match.domain.type.PitchArea;
import lombok.Builder;

import java.util.List;

@Builder
public class Duel {

    private Player attacker;
    private Player defender;
    private DuelType type;
    private PitchArea pitchArea;
    // Always from the attacker point of view
    private List<ModifierMetaInfo<DuelModifier>> modifiers;
    private DuelResult result;

    public static Duel generate(DuelStartedEvent duelStartedEvent) {
        Player attacker = duelStartedEvent.getAttacker();
        Player defender = duelStartedEvent.getDefender();
        DuelStrategy duelStrategy = DuelStrategyFactory.create(duelStartedEvent.getDuelType());
        // TODO: Include modifiers
        DuelResult duelResult = duelStrategy.execute(attacker.getActualSkills(), defender.getActualSkills());

        return Duel.builder()
                .attacker(attacker)
                .defender(defender)
                .type(duelStartedEvent.getDuelType())
                .pitchArea(duelStartedEvent.getPitchArea())
                .modifiers(duelStartedEvent.getModifiers())
                .result(duelResult)
                .build();
    }

    public boolean isWin() {
        return result == DuelResult.WIN;
    }

    public boolean isLose() {
        return result == DuelResult.LOSE;
    }
}
