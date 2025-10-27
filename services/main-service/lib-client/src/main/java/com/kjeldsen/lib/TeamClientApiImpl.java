package com.kjeldsen.lib;

import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.lib.model.team.TeamClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class TeamClientApiImpl extends BaseClientApiImpl implements TeamClientApi {

    public TeamClientApiImpl(WebClient webClient) {
        super(webClient);
    }

    @Override
    public List<TeamClient> getTeam(String teamId, String teamName, String userId) {
        String uri = buildUri(teamId, teamName, userId);
        if (teamId != null) {
            TeamClient response = executeRequestMono(uri, new ParameterizedTypeReference<>() {});
            return response != null ? Collections.singletonList(response) : Collections.emptyList();
        } else {
            List<TeamClient> response = executeRequestFlux(uri, new ParameterizedTypeReference<>() {});
            return response != null ? response : Collections.emptyList();
        }
    }

    @Override
    protected String buildUri(String... params) {
        if (params[0] != null ) {
            return UriComponentsBuilder.fromPath("/team/{teamId}")
                .buildAndExpand(params[0])
                .toUriString();
        } else {
            return  UriComponentsBuilder.fromPath("/team")
                .queryParamIfPresent("name", Optional.ofNullable(params[1]))
                .queryParamIfPresent("userId", Optional.ofNullable(params[2]))
                .queryParam("size", 10)
                .queryParam("page", 0)
                .toUriString();
        }
    }
}
