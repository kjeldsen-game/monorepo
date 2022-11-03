package com.kjeldsen.player.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

@AllArgsConstructor
@Getter
public enum PlayerPosition {
    DEFENDER(Map.of(PlayerSkill.TACKLING, 5,
        PlayerSkill.DEFENSE_POSITION, 5,
        PlayerSkill.CO, 2,
        PlayerSkill.SCORE, 1,
        PlayerSkill.OFFENSIVE_POSITION, 1,
        PlayerSkill.BALL_CONTROL, 1,
        PlayerSkill.PASSING, 1
    )),
    MIDDLE(Map.of(PlayerSkill.PASSING, 4,
        PlayerSkill.BALL_CONTROL, 3,
        PlayerSkill.OFFENSIVE_POSITION, 3,
        PlayerSkill.CO, 2,
        PlayerSkill.TACKLING, 2,
        PlayerSkill.DEFENSE_POSITION, 2,
        PlayerSkill.SCORE, 1
    )),
    FORWARD(Map.of(PlayerSkill.SCORE, 5,
        PlayerSkill.OFFENSIVE_POSITION, 4,
        PlayerSkill.BALL_CONTROL, 4,
        PlayerSkill.PASSING, 2,
        PlayerSkill.CO, 2,
        PlayerSkill.TACKLING, 1,
        PlayerSkill.DEFENSE_POSITION, 1
    ));

    private final Map<PlayerSkill, Integer> tendencies;

    public Optional<PlayerSkill> getRandomSkillBasedOnTendency(Set<PlayerSkill> excludedSkills) {
        // Creating list with the distribution of skills based on tendency value.
        // For example, having FORWARD and no excluded skill, the resulting list will be:
        // [SCORE, SCORE, SCORE, SCORE, SCORE,
        //  OFFENSIVE_POSITION, OFFENSIVE_POSITION, OFFENSIVE_POSITION, OFFENSIVE_POSITION,
        //  BALL_CONTROL, BALL_CONTROL, BALL_CONTROL, BALL_CONTROL,
        //  PASSING, PASSING,
        //  CO, CO,
        //  TACKLING,
        //  DEFENSE_POSITION]
        List<PlayerSkill> distributedSkills = tendencies.entrySet().stream()
            .filter(entry -> !excludedSkills.contains(entry.getKey()))
            .flatMap(entry -> IntStream.range(0, entry.getValue())
                .mapToObj(i -> entry.getKey())
            )
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        return distributedSkills.isEmpty()
            ? Optional.empty()
            : Optional.of(distributedSkills.get(RandomUtils.nextInt(0, distributedSkills.size())));
    }
}
