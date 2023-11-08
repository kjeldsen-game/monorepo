package com.kjeldsen.player.domain.provider;

import com.github.javafaker.Faker;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerCategory;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.domain.Team;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Range;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerProvider {

    private static final Integer MIN_AGE = 15;
    private static final Integer MAX_AGE = 33;
    private static final Range<Integer> RANGE_OF_AGE = Range.between(MIN_AGE, MAX_AGE);
    public static final int MAX_SKILL_VALUE = 100;

    public static String name() {
        Faker faker = new Faker();
        return faker.name().fullName();
    }

    public static Integer age() {
        return RandomUtils.nextInt(RANGE_OF_AGE.getMinimum(), RANGE_OF_AGE.getMaximum());
    }

    public static Map<PlayerSkill, PlayerSkills> skillsBasedOnTendency(PlayerPositionTendency positionTendencies, Integer totalPoints) {

        HashMap<PlayerSkill, PlayerSkills> skillPointsMap = positionTendencies
            .getTendencies()
            .keySet()
            .stream()
            .collect(HashMap::new, (map, skill) -> map.put(skill, PlayerSkills.builder()
                .actual(0)
                .potential(0)
                .build()), HashMap::putAll);

        Set<PlayerSkill> excludedSkills = new HashSet<>();

        IntStream.range(0, totalPoints).forEach(i -> {
            Optional<PlayerSkill> skill = positionTendencies.getRandomSkillBasedOnTendency(excludedSkills);
            // if no skill is found, it means that all skills have reached the max value
            if (skill.isEmpty()) {
                return;
            }

            PlayerSkills values = skillPointsMap.get(skill.get());
            values.increaseActualPoints(1);

            if (values.getActual() == MAX_SKILL_VALUE) {
                excludedSkills.add(skill.get());
            }
            skillPointsMap.put(skill.get(), values);
        });

        skillPointsMap.values().forEach(PlayerSkills::initializePotentialPoints);

        return skillPointsMap;
    }

    public static PlayerSkill randomSkill() {
        PlayerSkill[] allSkills = PlayerSkill.values();
        int random = (int) (Math.random() * allSkills.length);
        return allSkills[random];
    }

    public static Player generate(Team.TeamId teamId, PlayerPositionTendency positionTendencies, PlayerCategory playerCategory, int totalPoints) {
        Player player = Player.builder()
            .id(Player.PlayerId.generate())
            .name(name())
            .age(age())
            .position(positionTendencies.getPosition())
            .actualSkills(skillsBasedOnTendency(positionTendencies, totalPoints))
            .teamId(teamId)
            .category(playerCategory)
            .economy(Player.Economy.builder().build())
            .build();
        player.negotiateSalary();
        return player;
    }

    public static Player generateDefault() {
        return generate(Team.TeamId.generate(), PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES, PlayerCategory.JUNIOR, 200);
    }

    public static PlayerPosition position() {
        return PlayerPosition.values()[RandomUtils.nextInt(0, PlayerPosition.values().length)];
    }

}
