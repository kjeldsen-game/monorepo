package com.kjeldsen.player.domain;

import com.kjeldsen.player.domain.events.PlayerBloomEvent;
import com.kjeldsen.player.domain.events.PlayerDeclineEvent;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.Range;

@Data
@Builder
@ToString
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

    public boolean isBloomActive(PlayerBloomEvent playerBloomEvent) {
        int initialRange = playerBloomEvent.getBloomStartAge();
        int endRange = initialRange + playerBloomEvent.getYearsOn();
        return Range.between(initialRange, endRange).contains(age.value());
    }

    public boolean isDeclineActive(PlayerDeclineEvent playerDeclineEvent) {
        int initialRange = playerDeclineEvent.getDeclineStartAge();
        int endRange = initialRange + playerDeclineEvent.getYearsOn();
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
