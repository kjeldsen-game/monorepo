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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public final class TeamGenerator {

    private static final Faker faker = new Faker();
    private static final AtomicLong idSequence = new AtomicLong(1);

    public static Team randomTeam() {
        Team team = Team.builder().id(idSequence.getAndIncrement()).build();
        int rating = new Random().nextInt(40, 90);
        return Team.builder()
            .id(team.getId())
            .players(defaultPlayers(team))
            .tactic(randomEnum(Tactic.class))
            .verticalPressure(randomEnum(VerticalPressure.class))
            .horizontalPressure(randomEnum(HorizontalPressure.class))
            .rating(rating)
            .fans(defaultFans(rating))
            .build();
    }

    public static Team teamFromPositions(List<PlayerPosition> positions) {
        Team team = Team.builder().id(idSequence.getAndIncrement()).build();
        List<Player> players = positions.stream()
            .map(pos -> playerAt(team, pos))
            .collect(Collectors.toList());
        int rating = new Random().nextInt(40, 90);
        return Team.builder()
            .id(team.getId())
            .players(players)
            .tactic(Tactic.MAN_ON_MAN)
            .verticalPressure(VerticalPressure.MID_PRESSURE)
            .horizontalPressure(HorizontalPressure.NO_HORIZONTAL_FOCUS)
            .rating(rating)
            .fans(defaultFans(rating))
            .build();
    }

    // Distributes 10,000 fans across tiers 1–5 based on team rating (40–90 range).
    // Higher-rated teams skew toward higher tiers (more loyal fans).
    static Map<Integer, Integer> defaultFans(int rating) {
        double loyalty = Math.max(0.0, Math.min(1.0, (rating - 40.0) / 50.0));
        int total = 10_000;
        int tier5 = (int) Math.round(total * (0.05 + 0.25 * loyalty));
        int tier4 = (int) Math.round(total * (0.10 + 0.15 * loyalty));
        int tier3 = (int) Math.round(total * (0.15 + 0.05 * loyalty));
        int tier2 = (int) Math.round(total * (0.25 - 0.10 * loyalty));
        int tier1 = total - tier5 - tier4 - tier3 - tier2;
        return Map.of(1, tier1, 2, tier2, 3, tier3, 4, tier4, 5, tier5);
    }

    private static List<Player> defaultPlayers(Team team) {
        return List.of(
            playerAt(team, PlayerPosition.GOALKEEPER),
            playerAt(team, PlayerPosition.LEFT_BACK),
            playerAt(team, PlayerPosition.CENTER_BACK),
            playerAt(team, PlayerPosition.RIGHT_BACK),
            playerAt(team, PlayerPosition.LEFT_MIDFIELDER),
            playerAt(team, PlayerPosition.CENTER_MIDFIELDER),
            playerAt(team, PlayerPosition.RIGHT_MIDFIELDER),
            playerAt(team, PlayerPosition.LEFT_WINGER),
            playerAt(team, PlayerPosition.RIGHT_WINGER),
            playerAt(team, PlayerPosition.FORWARD),
            playerAt(team, PlayerPosition.STRIKER)
        );
    }

    private static Player playerAt(Team team, PlayerPosition position) {
        return Player.builder()
            .id(idSequence.getAndIncrement())
            .team(team)
            .name(faker.name().fullName())
            .position(position)
            .playerOrder(randomEnum(PlayerOrder.class))
            .skills(randomSkills())
            .build();
    }

    private static Map<SkillType, Integer> randomSkills() {
        Random rng = new Random();
        return Arrays.stream(SkillType.values())
            .collect(Collectors.toMap(s -> s, s -> rng.nextInt(30, 90)));
    }

    private static <T extends Enum<T>> T randomEnum(Class<T> cls) {
        T[] values = cls.getEnumConstants();
        return values[new Random().nextInt(values.length)];
    }
}
