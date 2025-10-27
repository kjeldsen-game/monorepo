package com.kjeldsen.player.domain.utils;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerStatus;

import java.util.List;
import java.util.stream.Stream;

public class PlayerUtils {

    public static Stream<Player> filterPlayersByStatus(List<Player> players, PlayerStatus status) {
        return players.stream().filter(player -> player.getStatus().equals(status));
    }
}
