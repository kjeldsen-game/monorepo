package com.kjeldsen.player.domain;


import java.util.HashMap;
import java.util.Map;

public record PlayerActualSkills(Map<PlayerSkill, Integer> values) {

    public static PlayerActualSkills of(PlayerPosition position) {
        //TODO: Apply specific abilities value of a position
        return new PlayerActualSkills(position.getSkillTendencies().stream()
            .collect(HashMap::new, (map, skill) -> map.put(skill, 0), HashMap::putAll));
    }

    public static PlayerActualSkills of(Map<PlayerSkill, Integer> values) {
        return new PlayerActualSkills(values);
    }

    public void addAbilityPoints(PlayerSkill skill, int points) {
        values.merge(skill, points, Integer::sum);
    }

    public int getAbilityPoints(PlayerSkill skill) {
        return values.get(skill);
    }

}
