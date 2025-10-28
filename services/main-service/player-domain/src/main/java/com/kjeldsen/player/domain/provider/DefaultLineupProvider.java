package com.kjeldsen.player.domain.provider;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerStatus;
import com.kjeldsen.player.domain.utils.PlayerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class DefaultLineupProvider {

    private static final Map<PlayerPosition, Integer> POSITION_COUNT_MAP = Map.of(
        PlayerPosition.GOALKEEPER, 1,
        PlayerPosition.LEFT_BACK, 1,
        PlayerPosition.CENTRE_BACK, 3,
        PlayerPosition.RIGHT_BACK, 1,
        PlayerPosition.LEFT_MIDFIELDER, 1,
        PlayerPosition.CENTRE_MIDFIELDER, 1,
        PlayerPosition.RIGHT_MIDFIELDER, 1,
        PlayerPosition.FORWARD, 2
    );

    public static void set(List<Player> players) {
        setActivePlayers(players);
        setBenchPlayers(players);
    }

    private static void setActivePlayers(List<Player> players) {
        for (PlayerPosition position : POSITION_COUNT_MAP.keySet()) {
            List<Player> options = players.stream().filter(player ->
                    player.getPreferredPosition().equals(position) &&  player.getStatus().equals(PlayerStatus.INACTIVE))
                .collect(Collectors.toCollection(ArrayList::new));

            Collections.shuffle(options);

            if (options.size() < POSITION_COUNT_MAP.get(position)) {
                int missing = POSITION_COUNT_MAP.get(position) - options.size();

                List<Player> candidates = players.stream().filter(player -> !player.getPreferredPosition().equals(PlayerPosition.GOALKEEPER)
                        && !options.contains(player) && player.getStatus().equals(PlayerStatus.INACTIVE))
                    .collect(Collectors.toCollection(ArrayList::new));

                Collections.shuffle(candidates);
                options.addAll(candidates.stream().limit(missing).toList());
            }

            options.stream().limit(POSITION_COUNT_MAP.get(position)).forEach(player -> {
                player.setPosition(position);
                player.setStatus(PlayerStatus.ACTIVE);
            });
        }
    }

    private static void setBenchPlayers(List<Player> players) {
        List<Player> inactive = PlayerUtils.filterPlayersByStatus(players, PlayerStatus.INACTIVE)
            .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(inactive);
        inactive.stream().limit(7).forEach(player -> player.setStatus(PlayerStatus.BENCH));
    }
}
