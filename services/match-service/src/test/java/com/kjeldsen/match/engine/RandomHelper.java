package com.kjeldsen.match.engine;

import com.github.javafaker.Faker;
import com.kjeldsen.match.engine.entities.PlayerPosition;
import com.kjeldsen.match.engine.entities.SkillType;
import com.kjeldsen.match.engine.modifers.HorizontalPressure;
import com.kjeldsen.match.engine.modifers.PlayerOrder;
import com.kjeldsen.match.engine.modifers.Tactic;
import com.kjeldsen.match.engine.modifers.VerticalPressure;
import com.kjeldsen.match.models.Player;
import com.kjeldsen.match.models.Team;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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
        players.add(genPlayerWithPosition(team, PlayerPosition.CENTER_BACK));
        players.add(genPlayerWithPosition(team, PlayerPosition.LEFT_MIDFIELDER));
        players.add(genPlayerWithPosition(team, PlayerPosition.RIGHT_MIDFIELDER));
        players.add(genPlayerWithPosition(team, PlayerPosition.CENTER_MIDFIELDER));
        players.add(genPlayerWithPosition(team, PlayerPosition.LEFT_WINGER));
        players.add(genPlayerWithPosition(team, PlayerPosition.RIGHT_WINGER));
        players.add(genPlayerWithPosition(team, PlayerPosition.STRIKER));
        players.add(genPlayerWithPosition(team, PlayerPosition.STRIKER));
        players.add(genPlayerWithPosition(team, PlayerPosition.GOALKEEPER));
        return players;
    }

    public static Player genPlayerWithPosition(Team team, PlayerPosition position) {
        return Player.builder()
            .id(new Random().nextLong())
            .team(team)
            .name(faker.name().fullName())
            .position(position)
            .playerOrder(genPlayerOrder())
            .skills(genSkillSet()).build();
    }


    public static Player genPlayer(Team team) {
        return Player.builder()
            .id(new Random().nextLong())
            .team(team)
            .name(faker.name().fullName())
            .position(PlayerPosition.values()[new Random().nextInt(PlayerPosition.values().length)])
            .skills(genSkillSet()).build();
    }

    private static Map<SkillType, Integer> genSkillSet() {
        return Arrays.stream(SkillType.values())
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
            .id(new Random().nextLong())
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
