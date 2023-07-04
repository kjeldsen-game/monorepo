package com.kjeldsen.match.domain.aggregate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
public class Player {

    public static final int MAX_SKILL_VALUE = 100;
    public static final int MIN_SKILL_VALUE = 0;

    private PlayerId id;
    private String name;
    private Integer age;
    private PlayerPosition position;
    private Map<PlayerSkill, Integer> actualSkills;
    private String teamId;

    public Integer getActualSkillPoints(PlayerSkill skill) {
        return actualSkills.get(skill);
    }

    public void addSkillPoints(PlayerSkill skill, Integer points) {
        actualSkills.put(skill, Math.min(MAX_SKILL_VALUE, getActualSkillPoints(skill) + points));
    }

    public void subtractSkillPoints(PlayerSkill skill, Integer points) {
        actualSkills.put(skill, Math.max(MIN_SKILL_VALUE, getActualSkillPoints(skill) - points));
    }

    public record PlayerId(String value) {
        public static PlayerId generate() {
            return new PlayerId(UUID.randomUUID().toString());
        }

        public static PlayerId of(String value) {
            return new PlayerId(value);
        }
    }


    @Getter
    public enum PlayerPosition {
        DEFENDER,
        MIDDLE,
        FORWARD;
    }

    public enum PlayerSkill {
        SCORE,
        OFFENSIVE_POSITION,
        BALL_CONTROL,
        PASSING,
        CO,
        TACKLING,
        DEFENSE_POSITION
    }
}
