package com.kjeldsen.player.domain;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.stream.IntStream;

@Builder
@Data
@Document(collection = "PlayerPositionTendencies")
@TypeAlias("PlayerPositionTendency")

public class PlayerPositionTendency {

    public static final PlayerPositionTendency DEFAULT_CENTRE_BACK_TENDENCIES = PlayerPositionTendency.builder()
        .position(PlayerPosition.CENTRE_BACK)
        .tendencies(Map.of(
            PlayerSkill.SCORE, 1,
            PlayerSkill.OFFENSIVE_POSITION, 1,
            PlayerSkill.BALL_CONTROL, 2,
            PlayerSkill.PASSING, 2,
            PlayerSkill.AERIAL, 4,
            PlayerSkill.CO, 4,
            PlayerSkill.TACKLING, 8,
            PlayerSkill.DEFENSE_POSITION, 8
        )).build();

    public static final PlayerPositionTendency DEFAULT_AERIAL_CENTRE_BACK_TENDENCIES = PlayerPositionTendency.builder()
        .position(PlayerPosition.AERIAL_CENTRE_BACK)
        .tendencies(Map.of(
            PlayerSkill.SCORE, 1,
            PlayerSkill.OFFENSIVE_POSITION, 1,
            PlayerSkill.BALL_CONTROL, 2,
            PlayerSkill.PASSING, 2,
            PlayerSkill.AERIAL, 6,
            PlayerSkill.CO, 4,
            PlayerSkill.TACKLING, 5,
            PlayerSkill.DEFENSE_POSITION, 5
        )).build();

    public static final PlayerPositionTendency DEFAULT_FULL_BACK_TENDENCIES = PlayerPositionTendency.builder()
        .position(PlayerPosition.FULL_BACK)
        .tendencies(Map.of(
            PlayerSkill.SCORE, 1,
            PlayerSkill.OFFENSIVE_POSITION, 2,
            PlayerSkill.BALL_CONTROL, 2,
            PlayerSkill.PASSING, 4,
            PlayerSkill.AERIAL, 3,
            PlayerSkill.CO, 4,
            PlayerSkill.TACKLING, 8,
            PlayerSkill.DEFENSE_POSITION, 8
        )).build();

    public static final PlayerPositionTendency DEFAULT_FULL_WINGBACK_TENDENCIES = PlayerPositionTendency.builder()
        .position(PlayerPosition.FULL_WINGBACK)
        .tendencies(Map.of(
            PlayerSkill.SCORE, 1,
            PlayerSkill.OFFENSIVE_POSITION, 4,
            PlayerSkill.BALL_CONTROL, 4,
            PlayerSkill.PASSING, 5,
            PlayerSkill.AERIAL, 3,
            PlayerSkill.CO, 4,
            PlayerSkill.TACKLING, 6,
            PlayerSkill.DEFENSE_POSITION, 6
        )).build();

    public static final PlayerPositionTendency DEFAULT_DEFENSIVE_MIDFIELDER_TENDENCIES = PlayerPositionTendency.builder()
        .position(PlayerPosition.DEFENSIVE_MIDFIELDER)
        .tendencies(Map.of(
            PlayerSkill.SCORE, 1,
            PlayerSkill.OFFENSIVE_POSITION, 2,
            PlayerSkill.BALL_CONTROL, 3,
            PlayerSkill.PASSING, 6,
            PlayerSkill.AERIAL, 3,
            PlayerSkill.CO, 4,
            PlayerSkill.TACKLING, 6,
            PlayerSkill.DEFENSE_POSITION, 6
        )).build();

    public static final PlayerPositionTendency DEFAULT_CENTRE_MIDFIELDER_TENDENCIES = PlayerPositionTendency.builder()
        .position(PlayerPosition.CENTRE_MIDFIELDER)
        .tendencies(Map.of(
            PlayerSkill.SCORE, 1,
            PlayerSkill.OFFENSIVE_POSITION, 6,
            PlayerSkill.BALL_CONTROL, 6,
            PlayerSkill.PASSING, 8,
            PlayerSkill.AERIAL, 3,
            PlayerSkill.CO, 4,
            PlayerSkill.TACKLING, 4,
            PlayerSkill.DEFENSE_POSITION, 4
        )).build();

    public static final PlayerPositionTendency DEFAULT_OFFENSIVE_MIDFIELDER_TENDENCIES = PlayerPositionTendency.builder()
        .position(PlayerPosition.OFFENSIVE_MIDFIELDER)
        .tendencies(Map.of(
            PlayerSkill.SCORE, 3,
            PlayerSkill.OFFENSIVE_POSITION, 8,
            PlayerSkill.BALL_CONTROL, 8,
            PlayerSkill.PASSING, 6,
            PlayerSkill.AERIAL, 3,
            PlayerSkill.CO, 4,
            PlayerSkill.TACKLING, 1,
            PlayerSkill.DEFENSE_POSITION, 1
        )).build();

    public static final PlayerPositionTendency DEFAULT_FORWARD_TENDENCIES = PlayerPositionTendency.builder()
        .position(PlayerPosition.FORWARD)
        .tendencies(Map.of(
            PlayerSkill.SCORE, 7,
            PlayerSkill.OFFENSIVE_POSITION, 7,
            PlayerSkill.BALL_CONTROL, 7,
            PlayerSkill.PASSING, 3,
            PlayerSkill.AERIAL, 3,
            PlayerSkill.CO, 4,
            PlayerSkill.TACKLING, 1,
            PlayerSkill.DEFENSE_POSITION, 1
        )).build();

    public static final PlayerPositionTendency DEFAULT_AERIAL_FORWARD_TENDENCIES = PlayerPositionTendency.builder()
        .position(PlayerPosition.AERIAL_FORWARD)
        .tendencies(Map.of(
            PlayerSkill.SCORE, 5,
            PlayerSkill.OFFENSIVE_POSITION, 5,
            PlayerSkill.BALL_CONTROL, 5,
            PlayerSkill.PASSING, 3,
            PlayerSkill.AERIAL, 7,
            PlayerSkill.CO, 4,
            PlayerSkill.TACKLING, 1,
            PlayerSkill.DEFENSE_POSITION, 1
        )).build();

    public static final PlayerPositionTendency DEFAULT_GOALKEEPER_TENDENCIES = PlayerPositionTendency.builder()
        .position(PlayerPosition.GOALKEEPER)
        .tendencies(Map.of(
            PlayerSkill.REFLEXES, 5,
            PlayerSkill.POSITIONING, 4,
            PlayerSkill.INTERCEPTIONS, 2,
            PlayerSkill.CONTROL, 2,
            PlayerSkill.ORGANIZATION, 2,
            PlayerSkill.ONE_ON_ONE, 4
        ))
        .build();

    public static final List<PlayerPositionTendency> DEFAULT_TENDENCIES = List.of(
        DEFAULT_CENTRE_BACK_TENDENCIES,
        DEFAULT_AERIAL_CENTRE_BACK_TENDENCIES,
        DEFAULT_FULL_BACK_TENDENCIES,
        DEFAULT_FULL_WINGBACK_TENDENCIES,
        DEFAULT_DEFENSIVE_MIDFIELDER_TENDENCIES,
        DEFAULT_CENTRE_MIDFIELDER_TENDENCIES,
        DEFAULT_DEFENSIVE_MIDFIELDER_TENDENCIES,
        DEFAULT_CENTRE_MIDFIELDER_TENDENCIES,
        DEFAULT_OFFENSIVE_MIDFIELDER_TENDENCIES,
        DEFAULT_FORWARD_TENDENCIES,
        DEFAULT_AERIAL_FORWARD_TENDENCIES);

    public static PlayerPositionTendency getDefault(PlayerPosition position) {
        return switch (position) {
            case CENTRE_BACK -> DEFAULT_CENTRE_BACK_TENDENCIES;
            case AERIAL_CENTRE_BACK -> DEFAULT_AERIAL_CENTRE_BACK_TENDENCIES;
            case FULL_BACK -> DEFAULT_FULL_BACK_TENDENCIES;
            case FULL_WINGBACK -> DEFAULT_FULL_WINGBACK_TENDENCIES;
            case DEFENSIVE_MIDFIELDER -> DEFAULT_DEFENSIVE_MIDFIELDER_TENDENCIES;
            case CENTRE_MIDFIELDER -> DEFAULT_CENTRE_MIDFIELDER_TENDENCIES;
            case OFFENSIVE_MIDFIELDER -> DEFAULT_OFFENSIVE_MIDFIELDER_TENDENCIES;
            case FORWARD -> DEFAULT_FORWARD_TENDENCIES;
            case AERIAL_FORWARD -> DEFAULT_AERIAL_FORWARD_TENDENCIES;
            case GOALKEEPER -> DEFAULT_GOALKEEPER_TENDENCIES;
        };
    }

    private String id;
    private PlayerPosition position;
    private Map<PlayerSkill, Integer> tendencies;

    public Boolean isDefault() {
        return DEFAULT_TENDENCIES.contains(this);
    }

    public Optional<PlayerSkill> getRandomSkillBasedOnTendency(Set<PlayerSkill> excludedSkills) {
        List<PlayerSkill> distributedSkills = getDistributedSkills(excludedSkills);

        return distributedSkills.isEmpty()
            ? Optional.empty()
            : Optional.of(distributedSkills.get(RandomUtils.nextInt(0, distributedSkills.size())));
    }

    /**
     * Returns list with the distribution of skills based on tendency value.
     * For example, having DEFAULT_FORWARD_TENDENCIES and no excluded skill, the resulting list will be:
     * [SCORE, SCORE, SCORE, SCORE, SCORE,
     * OFFENSIVE_POSITION, OFFENSIVE_POSITION, OFFENSIVE_POSITION, OFFENSIVE_POSITION,
     * BALL_CONTROL, BALL_CONTROL, BALL_CONTROL, BALL_CONTROL,
     * PASSING, PASSING,
     * CO, CO,
     * TACKLING,
     * DEFENSE_POSITION]
     */
    private ArrayList<PlayerSkill> getDistributedSkills(Set<PlayerSkill> excludedSkills) {
        return tendencies.entrySet().stream()
            .filter(entry -> !excludedSkills.contains(entry.getKey()))
            .flatMap(entry -> IntStream.range(0, entry.getValue())
                .mapToObj(i -> entry.getKey())
            )
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
