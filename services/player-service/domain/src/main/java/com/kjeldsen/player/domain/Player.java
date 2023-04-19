package com.kjeldsen.player.domain;

import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.Range;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@Document(collection = "Players")
@TypeAlias("Player")
public class Player {

    public static final int MAX_SKILL_VALUE = 100;
    public static final int MIN_SKILL_VALUE = 0;

    @Id
    private PlayerId id;
    private String name;
    private Integer age;
    private PlayerPosition position;
    private Map<PlayerSkill, Integer> actualSkills;
    private Team.TeamId teamId;


    public boolean isBloomActive(PlayerTrainingBloomEvent playerTrainingBloomEvent) {
        int initialRange = playerTrainingBloomEvent.getBloomStartAge();
        int endRange = initialRange + playerTrainingBloomEvent.getYearsOn();
        return Range.between(initialRange, endRange).contains(age);
    }

    public boolean isDeclineActive(PlayerTrainingDeclineEvent playerTrainingDeclineEvent) {
        return age >= playerTrainingDeclineEvent.getDeclineStartAge();
    }

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
        public static com.kjeldsen.player.domain.Player.PlayerId generate() {
            return new com.kjeldsen.player.domain.Player.PlayerId(UUID.randomUUID().toString());
        }

        public static com.kjeldsen.player.domain.Player.PlayerId of(String value) {
            return new com.kjeldsen.player.domain.Player.PlayerId(value);
        }
    }

}
