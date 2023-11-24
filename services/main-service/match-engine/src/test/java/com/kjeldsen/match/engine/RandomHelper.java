package com.kjeldsen.match.engine;

import com.github.javafaker.Faker;
import com.kjeldsen.match.models.Team;
import com.kjeldsen.match.models.Player;
import com.kjeldsen.match.engine.entities.PlayerPosition;
import com.kjeldsen.match.engine.entities.SkillType;
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
        players.add(genPlayerWithPosition(team, PlayerPosition.LEFT_BACK)); //
        players.add(genPlayerWithPosition(team, PlayerPosition.RIGHT_BACK)); //
        players.add(genPlayerWithPosition(team, PlayerPosition.CENTER_BACK)); // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
        players.add(genPlayerWithPosition(team, PlayerPosition.LEFT_MIDFIELDER));
        players.add(genPlayerWithPosition(team, PlayerPosition.RIGHT_MIDFIELDER));
        players.add(genPlayerWithPosition(team, PlayerPosition.CENTER_MIDFIELDER)); // TODO NADIR I AM NOT SURE IF IS IMPORTANT BUT EDUARDO USUALLY WRITE CENTRE IN THE DOCUMENTATION
        players.add(genPlayerWithPosition(team, PlayerPosition.LEFT_WINGER));
        players.add(genPlayerWithPosition(team, PlayerPosition.RIGHT_WINGER));
        players.add(genPlayerWithPosition(team, PlayerPosition.STRIKER));
        players.add(genPlayerWithPosition(team, PlayerPosition.STRIKER)); // TODO NADIR MAYBE IS THIS REPEATED ? MAYBE YOU WANTED TO SAY "SWEEPER"?
        players.add(genPlayerWithPosition(team, PlayerPosition.GOALKEEPER));
        return players;
    }

    public static Player genPlayerWithPosition(Team team, PlayerPosition position) {
        return Player.builder()
            .id(new Random().nextLong())
            .team(team)
            .name(faker.name().fullName())
            .position(position)
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

    public static int genAttributeRating() {
        return new Random().nextInt(30, 100);
    }

    public static Team genTeam() {
        Team team = Team.builder()
            .id(new Random().nextLong())
            .build();
        List<Player> players = genPlayers(team);
        int rating = genAttributeRating();
        return Team.builder()
            .id(team.getId())
            .players(players)
            .rating(rating)
            .build();
    }
}
