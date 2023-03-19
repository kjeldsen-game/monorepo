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

@Getter
@Setter
@Builder
@ToString
@Document(collection = "Players")
@TypeAlias("Player")
public class Player {

    @Id
    private PlayerId id;
    private PlayerName name;
    private PlayerAge age;
    private PlayerPosition position;
    private PlayerActualSkills actualSkills;
    private TeamId teamId;

    public static Player generate(TeamId teamId, PlayerPositionTendency positionTendencies, int totalPoints) {
        return Player.builder()
            .id(PlayerId.generate())
            .name(PlayerName.generate())
            .age(PlayerAge.generate())
            .position(positionTendencies.getPosition())
            .actualSkills(PlayerActualSkills.generate(positionTendencies, totalPoints))
            .teamId(teamId)
            .build();
    }

    public boolean isBloomActive(PlayerTrainingBloomEvent playerTrainingBloomEvent) {
        int initialRange = playerTrainingBloomEvent.getBloomStartAge();
        int endRange = initialRange + playerTrainingBloomEvent.getYearsOn();
        return Range.between(initialRange, endRange).contains(age.value());
    }

    public boolean isDeclineActive(PlayerTrainingDeclineEvent playerTrainingDeclineEvent) {
        return age.value() >= playerTrainingDeclineEvent.getDeclineStartAge();
    }

    public Integer getActualSkillPoints(PlayerSkill skill) {
        return actualSkills.getSkillPoints(skill);
    }

    public void addSkillPoints(PlayerSkill skill, Integer points) {
        actualSkills.addSkillPoints(skill, points);
    }

    public void subtractSkillPoints(PlayerSkill skill, Integer points) {
        Integer actual = getActualSkillPoints(skill);
        actualSkills.addSkillPoints(skill, -points);
    }

}
