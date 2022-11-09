package com.kjeldsen.player.domain;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public record PlayerActualSkills(Map<PlayerSkill, Integer> values) {

    public static final int MAX_SKILL_VALUE = 100;

    public static PlayerActualSkills generate(PlayerPositionTendency positionTendencies, Integer totalPoints) {
        HashMap<PlayerSkill, Integer> values = positionTendencies.getTendencies().keySet().stream()
            .collect(HashMap::new, (map, skill) -> map.put(skill, 0), HashMap::putAll);
        Set<PlayerSkill> excludedSkills = new HashSet<>();

        for (int i = 0; i < totalPoints; i++) {
            Optional<PlayerSkill> skill = positionTendencies.getRandomSkillBasedOnTendency(excludedSkills);
            // if no skill is found, it means that all skills have reached the max value
            if (skill.isEmpty()) {
                break;
            }
            values.put(skill.get(), values.get(skill.get()) + 1);
            if (values.get(skill.get()) == MAX_SKILL_VALUE) {
                excludedSkills.add(skill.get());
            }
        }

        return new PlayerActualSkills(values);
    }

    public static PlayerActualSkills of(Map<PlayerSkill, Integer> values) {
        return new PlayerActualSkills(values);
    }

    public void addSkillPoints(PlayerSkill skill, int points) {
        int normalizedPoints = Math.min(points, MAX_SKILL_VALUE - values.get(skill));
        values.merge(skill, normalizedPoints, Integer::sum);
    }

    public int getSkillPoints(PlayerSkill skill) {
        return values.get(skill);
    }

    public int getTotalPoints() {
        return values.values().stream().mapToInt(Integer::intValue).sum();
    }
}
