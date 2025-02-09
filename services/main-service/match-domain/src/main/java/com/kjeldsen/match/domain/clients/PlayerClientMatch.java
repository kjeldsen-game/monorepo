package com.kjeldsen.match.domain.clients;

import com.kjeldsen.match.domain.clients.models.player.PlayerDTO;

import java.util.List;

public interface PlayerClientMatch {
    List<PlayerDTO> getPlayers(String teamId, String token);
}
