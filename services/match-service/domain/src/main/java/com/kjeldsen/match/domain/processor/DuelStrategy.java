package com.kjeldsen.match.domain.processor;

import com.kjeldsen.match.domain.aggregate.Player;
import com.kjeldsen.match.domain.type.DuelResult;

import java.util.Map;

public interface DuelStrategy {
    DuelResult execute(Map<Player.PlayerSkill, Integer> attackerSkills, Map<Player.PlayerSkill, Integer> defenderSkills);
}
