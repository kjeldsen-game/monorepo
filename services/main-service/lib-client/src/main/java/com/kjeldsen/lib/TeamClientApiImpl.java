package com.kjeldsen.lib;

import com.kjeldsen.lib.auth.InternalClientTokenProvider;
import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.lib.clients.base.AuthenticatedClientApiImpl;
import com.kjeldsen.player.rest.model.TeamResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class TeamClientApiImpl extends AuthenticatedClientApiImpl implements TeamClientApi {

    public TeamClientApiImpl(WebClient webClient, InternalClientTokenProvider internalClientTokenProvider) {
        super(webClient, internalClientTokenProvider);
    }

    @Override
    public TeamResponse getTeamById(String teamId) {
        String uri = UriComponentsBuilder.fromPath("/team/{teamId}")
            .buildAndExpand(teamId).toUriString();
        return executeMono(() -> executeRequest(HttpMethod.GET, uri, null, null),
            new ParameterizedTypeReference<>() {});
    }

    @Override
    public List<TeamResponse> getTeams(String teamName, String userId) {
        String uri = UriComponentsBuilder.fromPath("/team")
            .queryParamIfPresent("name", Optional.ofNullable(teamName))
            .queryParamIfPresent("userId", Optional.ofNullable(userId))
            .queryParam("size", 10)
            .queryParam("page", 0)
            .toUriString();

        log.info("Get team uri constructed: {}", uri);
        return executeFlux(() -> executeRequest(HttpMethod.GET, uri, null, null),
            new ParameterizedTypeReference<>() {});
    }
}
