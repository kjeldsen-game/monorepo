package com.kjeldsen.player.domain;

import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.Range;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@ToString
@Document(collection = "Players")
@TypeAlias("Player")
public class Player {

    public static Player generate(PlayerPositionTendency positionTendencies, int totalPoints) {
        return Player.builder()
            .id(PlayerId.generate())
            .name(PlayerName.generate())
            .age(PlayerAge.generate())
            .position(positionTendencies.getPosition())
            .actualSkills(PlayerActualSkills.generate(positionTendencies, totalPoints))
            .build();
    }

    private PlayerId id;
    private PlayerName name;
    private PlayerAge age;
    private PlayerPosition position;
    private PlayerActualSkills actualSkills;

    public boolean isBloomActive(PlayerTrainingBloomEvent playerTrainingBloomEvent) {
        int initialRange = playerTrainingBloomEvent.getBloomStartAge();
        int endRange = initialRange + playerTrainingBloomEvent.getYearsOn();
        return Range.between(initialRange, endRange).contains(age.value());
    }

    public boolean isDeclineActive(PlayerTrainingDeclineEvent playerTrainingDeclineEvent) {
        int initialRange = playerTrainingDeclineEvent.getDeclineStartAge();
        int endRange = initialRange + playerTrainingDeclineEvent.getYearsOn();
        return Range.between(initialRange, endRange).contains(age.value());
    }

    public Integer getActualSkillPoints(PlayerSkill skill) {
        return actualSkills.getSkillPoints(skill);
    }

    public void addSkillPoints(PlayerSkill skill, Integer points) {
        Integer actual = getActualSkillPoints(skill);
        actualSkills.addSkillPoints(skill, actual + points);
    }

    public void subtractSkillPoints(PlayerSkill skill, Integer points) {
        Integer actual = getActualSkillPoints(skill);
        actualSkills.addSkillPoints(skill, actual - points);
    }
}
