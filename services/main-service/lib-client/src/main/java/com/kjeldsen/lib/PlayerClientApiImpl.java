package com.kjeldsen.lib;

import com.kjeldsen.lib.clients.PlayerClientApi;
import com.kjeldsen.lib.clients.base.AuthenticatedClientApiImpl;
import com.kjeldsen.player.rest.model.PlayerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@Slf4j
public class PlayerClientApiImpl extends AuthenticatedClientApiImpl implements PlayerClientApi {

    public PlayerClientApiImpl(WebClient webClient, InternalClientTokenProvider internalClientTokenProvider) {
        super(webClient, internalClientTokenProvider);
    }

    @Override
    public PlayerResponse getPlayer(String playerId) {
        String uri =  UriComponentsBuilder.fromPath("/player/{playerId}")
            .buildAndExpand(playerId)
            .toUriString();

        return executeMono(() -> executeRequest(HttpMethod.GET, uri, null, null),
            new ParameterizedTypeReference<>() {});
    }

    @Override
    public List<PlayerResponse> getPlayers(String teamId) {
        String uri = UriComponentsBuilder.fromPath("/player").queryParam("teamId", teamId)
            .queryParam("size", 50)
            .queryParam("page", 0)
            .build().toUriString();
        log.info("GetPlayers uri =");
        return executeFlux(() -> executeRequest(HttpMethod.GET, uri ,null, null), new ParameterizedTypeReference<>() {});
    }
}
