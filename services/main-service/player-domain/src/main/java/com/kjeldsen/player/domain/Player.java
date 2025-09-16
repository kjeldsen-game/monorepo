package com.kjeldsen.player.domain;

import static com.kjeldsen.player.domain.exceptions.BusinessValidationException.throwIfNot;
import static com.kjeldsen.player.domain.exceptions.ErrorCodes.DECLINE_PLAYER_AGE_INVALID_RANGE;

import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.generator.SalaryGenerator;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.kjeldsen.player.domain.models.training.TrainingEvent;
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
    private PlayerPosition preferredPosition;
    private PlayerStatus status;
    private PlayerOrder playerOrder;
    private Map<PlayerSkill, PlayerSkills> actualSkills;
    private Team.TeamId teamId;
    private Integer bloomYear;
    private PitchArea playerOrderDestinationPitchArea;

    //    private PlayerTrainingBloomEvent bloom;
//    private PlayerTrainingDeclineEvent decline;
    @Builder.Default
    private boolean isFallCliff = false;
    private PlayerCategory category;
    private Economy economy;

    public void addDeclinePhase(PlayerTrainingDeclineEvent playerTrainingDeclineEvent) {
        throwIfNot(Range.between(MIN_DECLINE_PLAYER_AGE, MAX_DECLINE_PLAYER_AGE).contains(age.getYears()), DECLINE_PLAYER_AGE_INVALID_RANGE);
        subtractSkillPoints(playerTrainingDeclineEvent.getSkill(), playerTrainingDeclineEvent.getPointsToSubtract());
//        this.decline = playerTrainingDeclineEvent;
    }

    public void addDeclinePhase(TrainingEvent trainingEvent) {
        throwIfNot(Range.between(MIN_DECLINE_PLAYER_AGE, MAX_DECLINE_PLAYER_AGE).contains(age.getYears()), DECLINE_PLAYER_AGE_INVALID_RANGE);
        subtractSkillPoints(trainingEvent.getSkill(), trainingEvent.getPoints());
//        this.decline = trainingEvent;
    }


    public Integer getActualSkillPoints(PlayerSkill skill) {
        return getActualSkills().get(skill).getActual();
    }

    public Integer getPotentialSkillPoints(PlayerSkill skill) {
        return getActualSkills().get(skill).getPotential();
    }

    public boolean isBloom() {
        return this.bloomYear != null && Objects.equals(this.bloomYear, this.age.getYears());
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

    @Builder
    @Getter
    public static class Economy {
        private BigDecimal salary;
    }

}
