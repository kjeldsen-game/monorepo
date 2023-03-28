package com.kjeldsen.player.domain;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

@Builder
@Data
@Document(collection = "PlayerPositionTendencies")
@TypeAlias("PlayerPositionTendency")
public class PlayerPositionTendency {
    public static final PlayerPositionTendency DEFAULT_DEFENDER_TENDENCIES = PlayerPositionTendency.builder()
        .position(PlayerPosition.DEFENDER)
        .tendencies(Map.of(PlayerSkill.TACKLING, 5,
            PlayerSkill.DEFENSE_POSITION, 5,
            PlayerSkill.CO, 2,
            PlayerSkill.SCORE, 1,
            PlayerSkill.OFFENSIVE_POSITION, 1,
            PlayerSkill.BALL_CONTROL, 1,
            PlayerSkill.PASSING, 1
        ))
        .build();
    public static final PlayerPositionTendency DEFAULT_MIDDLE_TENDENCIES = PlayerPositionTendency.builder()
        .position(PlayerPosition.MIDDLE)
        .tendencies(Map.of(PlayerSkill.PASSING, 4,
            PlayerSkill.BALL_CONTROL, 3,
            PlayerSkill.OFFENSIVE_POSITION, 3,
            PlayerSkill.CO, 2,
            PlayerSkill.TACKLING, 2,
            PlayerSkill.DEFENSE_POSITION, 2,
            PlayerSkill.SCORE, 1
        ))
        .build();
    public static final PlayerPositionTendency DEFAULT_FORWARD_TENDENCIES = PlayerPositionTendency.builder()
        .position(PlayerPosition.FORWARD)
        .tendencies(Map.of(PlayerSkill.SCORE, 5,
            PlayerSkill.OFFENSIVE_POSITION, 4,
            PlayerSkill.BALL_CONTROL, 4,
            PlayerSkill.PASSING, 2,
            PlayerSkill.CO, 2,
            PlayerSkill.TACKLING, 1,
            PlayerSkill.DEFENSE_POSITION, 1
        ))
        .build();

    public static final List<PlayerPositionTendency> DEFAULT_TENDENCIES = List.of(DEFAULT_DEFENDER_TENDENCIES, DEFAULT_MIDDLE_TENDENCIES,
        DEFAULT_FORWARD_TENDENCIES);

    public static PlayerPositionTendency getDefault(PlayerPosition position) {
        return switch (position) {
            case DEFENDER -> DEFAULT_DEFENDER_TENDENCIES;
            case MIDDLE -> DEFAULT_MIDDLE_TENDENCIES;
            case FORWARD -> DEFAULT_FORWARD_TENDENCIES;
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
