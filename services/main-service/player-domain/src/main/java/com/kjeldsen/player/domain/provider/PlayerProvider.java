package com.kjeldsen.player.domain.provider;

import com.github.javafaker.Faker;
import com.kjeldsen.player.domain.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Range;

import java.util.*;
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

    public static Map<SkillType, Map<PlayerSkill, PlayerSkills>> skillsBasedOnTendency(PlayerPositionTendency positionTendencies, Integer totalPoints) {

        Map<SkillType, Map<PlayerSkill, PlayerSkills>> tendenciesAlternative = positionTendencies
                .getTendencies();
        Map<SkillType, Map<PlayerSkill, PlayerSkills>> initialTendencies = new HashMap<>();

        tendenciesAlternative.forEach((skillType, playerSkillValues) -> {
            Map<PlayerSkill, PlayerSkills> initialPlayerSkillValues = new HashMap<>();
            playerSkillValues.forEach((skill, value) -> initialPlayerSkillValues.put(skill, PlayerSkills.empty()));
            initialTendencies.put(skillType, initialPlayerSkillValues);
        });


        Set<PlayerSkill> excludedSkills = new HashSet<>();

        IntStream.range(0, totalPoints).forEach(i -> {
            Optional<PlayerSkill> skill = positionTendencies.getRandomSkillBasedOnTendency(excludedSkills);
            // if no skill is found, it means that all skills have reached the max value

            if (skill.isEmpty()) {
                return;
            }
            PlayerSkills values = initialTendencies.values().stream().filter(map -> map.containsKey(skill.get())).map(map -> map.get(skill.get())).findFirst().orElse(PlayerSkills.empty());
            values.increaseActualPoints(1);

            if (values.getActual() == MAX_SKILL_VALUE) {
                excludedSkills.add(skill.get());
            }
            initialTendencies.values().stream().filter(map -> map.containsKey(skill.get())).findFirst().get().put(skill.get(), values);
        });

        initialTendencies.values().forEach(playerSkillValues -> playerSkillValues.values().forEach(PlayerSkills::initializePotentialPoints));

        return initialTendencies;
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
                .actualSkillsAlternative(skillsBasedOnTendency(positionTendencies, totalPoints))
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
