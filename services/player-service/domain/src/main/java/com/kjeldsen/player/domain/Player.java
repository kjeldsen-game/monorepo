package com.kjeldsen.player.domain;

import com.kjeldsen.player.domain.events.PlayerCreationEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.generator.PointsGenerator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.Range;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.kjeldsen.player.domain.exception.BusinessValidationException.throwIfNot;
import static com.kjeldsen.player.domain.exception.ErrorCodes.*;

@Getter
@Setter
@Builder
@ToString
@Document(collection = "Players")
@TypeAlias("Player")
public class Player {

    public static final int MAX_SKILL_VALUE = 100;
    public static final int MIN_SKILL_VALUE = 0;

    public static final Integer MIN_BLOOM_PLAYER_AGE = 15;
    public static final Integer MAX_BLOOM_PLAYER_AGE = 30;
    public static final Integer MIN_BLOOM_YEARS_ON = 1;
    public static final Integer MAX_BLOOM_YEARS_ON = 10;
    private static final Integer MIN_BLOOM_SPEED = 0;
    private static final Integer MAX_BLOOM_SPEED = 1000;
    private static final Integer MIN_DECLINE_PLAYER_AGE = 15;
    private static final Integer MAX_DECLINE_PLAYER_AGE = 33;
    private static final Integer MIN_DECLINE_SPEED = 0;
    private static final Integer MAX_DECLINE_SPEED = 1000;

    @Id
    private PlayerId id;
    private String name;
    private Integer age;
    private PlayerPosition position;
    // TODO 72-add-potentials-to-the-player change Integer for a wrapper object to save moved current points and potential points
    // Refactorizar esto. Las actualSkills son el conjunto de los skillPoints y los potencialPoints
    private Map<PlayerSkill, PlayerSkills> actualSkills;
    private Team.TeamId teamId;
    private PlayerTrainingBloomEvent bloom;
    private PlayerTrainingDeclineEvent decline;

    public static Player creation(PlayerCreationEvent playerCreationEvent) {
        return Player.builder()
            .id(playerCreationEvent.getPlayerId())
            .name(playerCreationEvent.getName())
            .age(playerCreationEvent.getAge())
            .position(playerCreationEvent.getPosition())
            .actualSkills(playerCreationEvent.getInitialSkills())
            .teamId(playerCreationEvent.getTeamId())
            .build();
    }

    public boolean isBloomActive() {
        if (Objects.isNull(bloom)) {
            return false;
        }
        int initialRange = bloom.getBloomStartAge();
        int endRange = initialRange + bloom.getYearsOn();
        return Range.between(initialRange, endRange).contains(age);
    }

    public void addBloomPhase(PlayerTrainingBloomEvent playerTrainingBloomEvent) {
        throwIfNot(Range.between(MIN_BLOOM_PLAYER_AGE, MAX_BLOOM_PLAYER_AGE).contains(age), BLOOM_PLAYER_AGE_INVALID_RANGE);
        throwIfNot(Range.between(MIN_BLOOM_YEARS_ON, MAX_BLOOM_YEARS_ON).contains(playerTrainingBloomEvent.getYearsOn()),
            BLOOM_YEARS_ON_INVALID_RANGE);
        throwIfNot(Range.between(MIN_BLOOM_SPEED, MAX_BLOOM_SPEED).contains(playerTrainingBloomEvent.getBloomSpeed()),
            BLOOM_SPEED_INVALID_RANGE);

        this.bloom = playerTrainingBloomEvent;
    }

    public void addDeclinePhase(PlayerTrainingDeclineEvent playerTrainingDeclineEvent) {
        throwIfNot(Range.between(MIN_DECLINE_PLAYER_AGE, MAX_DECLINE_PLAYER_AGE).contains(age), DECLINE_PLAYER_AGE_INVALID_RANGE);
        throwIfNot(Range.between(MIN_DECLINE_SPEED, MAX_DECLINE_SPEED).contains(playerTrainingDeclineEvent.getDeclineSpeed()),
            DECLINE_SPEED_INVALID_RANGE);

        final int decreasePoints = PointsGenerator.generateDecreasePoints(
            playerTrainingDeclineEvent.getDeclineSpeed(),
            playerTrainingDeclineEvent.getPointsToSubtract());

        subtractSkillPoints(playerTrainingDeclineEvent.getSkill(), decreasePoints);
        this.decline = playerTrainingDeclineEvent;
    }

    // No habría que setear el skillPoints a los puntos del tendencies?? esta mapeado

    public Integer getActualSkillPoints(PlayerSkill skill) {
        return actualSkills.get(skill).getActual();
    }

    // TODO 72-add-potentials-to-the-player this logic has to change
    public void addSkillsActualPoints(PlayerSkill skill, Integer actualPoints) {
        PlayerSkills skillPoints = actualSkills.get(skill);
        skillPoints.setActual(Math.min(MAX_SKILL_VALUE, actualPoints));
    }

    private void subtractSkillPoints(PlayerSkill skill, Integer points) {
        PlayerSkills skillPoints = actualSkills.get(skill);
        skillPoints.setActual(Math.max(MIN_SKILL_VALUE, getActualSkillPoints(skill) - points));
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
