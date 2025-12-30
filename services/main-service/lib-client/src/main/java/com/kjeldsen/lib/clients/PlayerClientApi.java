package com.kjeldsen.lib.clients;

import com.kjeldsen.lib.model.player.PlayerClient;

import java.util.List;

public interface PlayerClientApi {
    List<PlayerClient> getPlayers(String teamId, List<String> playerIds);
    PlayerClient getPlayer(String playerId);
}
