package com.kjeldsen.player.domain;

import lombok.Builder;
import lombok.ToString;

import java.util.Map;

@Builder
@ToString
public class Player {

    private String id;
    private String name;
    private int age;
    private PlayerPosition position;
    private Map<PlayerAbility, Integer> abilities;

    public void addAbilityPoints(PlayerAbility playerAbility, int points) {
        if (abilities.containsKey(playerAbility)) {
            increaseAbility(playerAbility, points);
        } else {
            addAbility(playerAbility, points);
        }
    }

    private void increaseAbility(PlayerAbility playerAbility, int points) {
        abilities.computeIfPresent(playerAbility, (key, val) -> val + points);
    }

    private void addAbility(PlayerAbility playerAbility, int points) {
        abilities.putIfAbsent(playerAbility, points);
    }

}
