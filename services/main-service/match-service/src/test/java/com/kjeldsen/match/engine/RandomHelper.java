package com.kjeldsen.match.engine;

import com.github.javafaker.Faker;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.modifers.HorizontalPressure;
import com.kjeldsen.player.domain.PlayerOrder;
import com.kjeldsen.match.modifers.Tactic;
import com.kjeldsen.match.modifers.VerticalPressure;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

public final class RandomHelper {

    /*
     * Generate random resources for testing
     */

    static final Faker faker = new Faker();

    // Returns 11 random players including goalkeeper and a striker
    public static List<Player> genPlayers(Team team) {
        List<Player> players = new ArrayList<>(11);
        players.add(genPlayerWithPosition(team, PlayerPosition.LEFT_BACK));
        players.add(genPlayerWithPosition(team, PlayerPosition.RIGHT_BACK));
        players.add(genPlayerWithPosition(team, PlayerPosition.CENTRE_BACK));
        players.add(genPlayerWithPosition(team, PlayerPosition.LEFT_MIDFIELDER));
        players.add(genPlayerWithPosition(team, PlayerPosition.RIGHT_MIDFIELDER));
        players.add(genPlayerWithPosition(team, PlayerPosition.CENTRE_MIDFIELDER));
        players.add(genPlayerWithPosition(team, PlayerPosition.LEFT_WINGER));
        players.add(genPlayerWithPosition(team, PlayerPosition.RIGHT_WINGER));
        players.add(genPlayerWithPosition(team, PlayerPosition.STRIKER));
        players.add(genPlayerWithPosition(team, PlayerPosition.STRIKER));
        players.add(genPlayerWithPosition(team, PlayerPosition.GOALKEEPER));
        return players;
    }

    public static Player genPlayerWithPosition(Team team, PlayerPosition position) {
        return Player.builder()
            .id(RandomStringUtils.random(5))
            .teamId(team.getId())
            .name(faker.name().fullName())
            .position(position)
            .playerOrder(genPlayerOrder())
            .skills(genSkillSet()).build();
    }


    public static Player genPlayer(Team team) {
        return Player.builder()
            .id(RandomStringUtils.random(5))
            .teamId(team.getId())
            .name(faker.name().fullName())
            .position(PlayerPosition.values()[new Random().nextInt(PlayerPosition.values().length)])
            .skills(genSkillSet()).build();
    }

    private static Map<PlayerSkill, Integer> genSkillSet() {
        return Arrays.stream(PlayerSkill.values())
            .map(skill -> Map.entry(skill, genAttributeRating()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static PlayerOrder genPlayerOrder() {
        return PlayerOrder.values()[new Random().nextInt(PlayerOrder.values().length)];
    }

    public static int genAttributeRating() {
        return new Random().nextInt(30, 100);
    }

    public static Team genTeam() {
        Team team = Team.builder()
            .id(RandomStringUtils.random(5))
            .build();
        List<Player> players = genPlayers(team);
        int rating = genAttributeRating();

        Tactic tactic = Tactic.values()[new Random().nextInt(Tactic.values().length)];
        VerticalPressure vp =
            VerticalPressure.values()[new Random().nextInt(VerticalPressure.values().length)];
        HorizontalPressure hp =
            HorizontalPressure.values()[new Random().nextInt(HorizontalPressure.values().length)];

        return Team.builder()
            .id(team.getId())
            .players(players)
            .tactic(tactic)
            .verticalPressure(vp)
            .horizontalPressure(hp)
            .rating(rating)
            .build();
    }
}
