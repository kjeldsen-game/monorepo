package com.kjeldsen.player.domain;

import static com.kjeldsen.player.domain.exception.BusinessValidationException.throwIfNot;
import static com.kjeldsen.player.domain.exception.ErrorCodes.BLOOM_PLAYER_AGE_INVALID_RANGE;
import static com.kjeldsen.player.domain.exception.ErrorCodes.BLOOM_SPEED_INVALID_RANGE;
import static com.kjeldsen.player.domain.exception.ErrorCodes.BLOOM_YEARS_ON_INVALID_RANGE;
import static com.kjeldsen.player.domain.exception.ErrorCodes.DECLINE_PLAYER_AGE_INVALID_RANGE;
import static com.kjeldsen.player.domain.exception.ErrorCodes.DECLINE_SPEED_INVALID_RANGE;

import com.kjeldsen.player.domain.events.PlayerCreationEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.generator.PointsGenerator;
import com.kjeldsen.player.domain.generator.SalaryGenerator;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
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
    private PlayerAge age;
    private PlayerPosition position;
    private PlayerStatus status;
    private PlayerOrder playerOrder;
    private Map<PlayerSkill, PlayerSkills> actualSkills;
    private Team.TeamId teamId;
    private PlayerTrainingBloomEvent bloom;
    private PlayerTrainingDeclineEvent decline;
    private PlayerCategory category;
    private Economy economy;

    public static Player creation(PlayerCreationEvent playerCreationEvent) {
        Player player = Player.builder()
            .id(playerCreationEvent.getPlayerId())
            .name(playerCreationEvent.getName())
            .age(playerCreationEvent.getAge())
            .position(playerCreationEvent.getPosition())
            .playerOrder(PlayerOrder.NONE)
            .status(PlayerStatus.INACTIVE)
            .actualSkills(playerCreationEvent.getInitialSkills())
            .teamId(playerCreationEvent.getTeamId())
            .category(playerCreationEvent.getPlayerCategory())
            .economy(Economy.builder().build())
            .build();
        player.negotiateSalary();
        return player;
    }

    public boolean isBloomActive() {
        if (Objects.isNull(bloom)) {
            return false;
        }
        int initialRange = bloom.getBloomStartAge();
        int endRange = initialRange + bloom.getYearsOn();
        return Range.between(initialRange, endRange).contains(age.getYears());
    }

    public void addBloomPhase(PlayerTrainingBloomEvent playerTrainingBloomEvent) {
        throwIfNot(Range.between(MIN_BLOOM_PLAYER_AGE, MAX_BLOOM_PLAYER_AGE).contains(age.getYears()), BLOOM_PLAYER_AGE_INVALID_RANGE);
        throwIfNot(Range.between(MIN_BLOOM_YEARS_ON, MAX_BLOOM_YEARS_ON).contains(playerTrainingBloomEvent.getYearsOn()), BLOOM_YEARS_ON_INVALID_RANGE);
        throwIfNot(Range.between(MIN_BLOOM_SPEED, MAX_BLOOM_SPEED) .contains(playerTrainingBloomEvent.getBloomSpeed()), BLOOM_SPEED_INVALID_RANGE);

        this.bloom = playerTrainingBloomEvent;
    }

    public void addDeclinePhase(PlayerTrainingDeclineEvent playerTrainingDeclineEvent) {
        throwIfNot(Range.between(MIN_DECLINE_PLAYER_AGE, MAX_DECLINE_PLAYER_AGE).contains(age.getYears()), DECLINE_PLAYER_AGE_INVALID_RANGE);
        throwIfNot(Range.between(MIN_DECLINE_SPEED, MAX_DECLINE_SPEED).contains(playerTrainingDeclineEvent.getDeclineSpeed()), DECLINE_SPEED_INVALID_RANGE);

        final int decreasePoints = PointsGenerator.generateDecreasePoints(
            playerTrainingDeclineEvent.getDeclineSpeed(),
            playerTrainingDeclineEvent.getPointsToSubtract());

        subtractSkillPoints(playerTrainingDeclineEvent.getSkill(), decreasePoints);
        this.decline = playerTrainingDeclineEvent;
    }

    public Integer getActualSkillPoints(PlayerSkill skill) {
        return getActualSkills().get(skill).getActual();
    }

    public Integer getPotentialSkillPoints(PlayerSkill skill) {
        return getActualSkills().get(skill).getPotential();
    }

    public void addSkillsPoints(PlayerSkill skill, Integer points) {
        PlayerSkills skillPoints = getActualSkills().get(skill);
        skillPoints.setActual(Math.min(MAX_SKILL_VALUE, skillPoints.getActual() + points));
    }

    public void addSkillsPotentialRisePoints(PlayerSkill skill, Integer points) {
        PlayerSkills skillPoints = getActualSkills().get(skill);
        skillPoints.setPotential(Math.min(MAX_SKILL_VALUE, skillPoints.getPotential() + points));
    }

    private void subtractSkillPoints(PlayerSkill skill, Integer points) {
        PlayerSkills skillPoints = getActualSkills().get(skill);
        skillPoints.setActual(Math.max(MIN_SKILL_VALUE, getActualSkillPoints(skill) - points));
    }

    public void negotiateSalary() {
        economy.salary = SalaryGenerator.salary(this);
    }

    public record PlayerId(String value) {

        public static com.kjeldsen.player.domain.Player.PlayerId generate() {
            return new com.kjeldsen.player.domain.Player.PlayerId(UUID.randomUUID().toString());
        }

        public static com.kjeldsen.player.domain.Player.PlayerId of(String value) {
            return new com.kjeldsen.player.domain.Player.PlayerId(value);
        }
    }

    public static void checkDifferenceOverflowPoints(PlayerSkill skill, Player player, Integer points) {
        Integer actualSkillPoint = player.getActualSkillPoints(skill);
        Integer potentialSkillPoint = player.getPotentialSkillPoints(skill);
        player.getActualSkills().get(skill).setActual(Math.min(potentialSkillPoint, actualSkillPoint + points));
    }

    @Builder
    @Getter
    public static class Economy {
        private BigDecimal salary;
    }

}
