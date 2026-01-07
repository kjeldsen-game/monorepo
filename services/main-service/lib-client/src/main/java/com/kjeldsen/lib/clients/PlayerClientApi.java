package com.kjeldsen.lib.clients;

import com.kjeldsen.player.rest.model.PlayerResponse;

import java.util.List;

public interface PlayerClientApi {

    List<PlayerResponse> getPlayers(String teamId);

    PlayerResponse getPlayer(String playerId);
}
