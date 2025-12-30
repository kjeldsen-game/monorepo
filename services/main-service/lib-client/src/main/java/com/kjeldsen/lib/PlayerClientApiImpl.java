package com.kjeldsen.lib;

import com.kjeldsen.lib.clients.PlayerClientApi;
import com.kjeldsen.lib.model.player.PlayerClient;
import com.kjeldsen.lib.model.team.TeamClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class PlayerClientApiImpl extends BaseClientApiImpl implements PlayerClientApi {

    public PlayerClientApiImpl(WebClient webClient) {
        super(webClient);
    }

    @Override
    public PlayerClient getPlayer(String playerId) {
        return executeRequestMono(
            UriComponentsBuilder.fromPath("/player/{playerId}")
                .buildAndExpand(playerId)
                .toUriString(), new ParameterizedTypeReference<>() {});
    }

    @Override
    public List<PlayerClient> getPlayers(String teamId, List<String> playerIds) {
        String uri = buildUri(teamId, playerIds);
        log.info("GetPlayers uri = {}", uri);
        return executeRequestFlux(uri, new ParameterizedTypeReference<>() {});
    }

    @Override
    protected String buildUri(String... params) {

        return UriComponentsBuilder.fromPath("/player")
            .queryParamIfPresent("teamId", params[0] != null ? Optional.of(params[0]) : Optional.empty())
            .queryParam("size", 100)
            .toUriString();
    }

    protected String buildUri(String teamId, List<String> playerIds) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/player");
        if (teamId != null) {
            builder.queryParam("teamId", teamId);
        }
        if (playerIds != null && !playerIds.isEmpty()) {
            playerIds.forEach(id -> builder.queryParam("playerId", id));
        }
        builder.queryParam("size", 100);
        builder.queryParam("page", 0);
        return builder.toUriString();
    }
}
