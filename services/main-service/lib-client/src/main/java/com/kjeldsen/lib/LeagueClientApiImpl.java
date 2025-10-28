package com.kjeldsen.lib;

import com.kjeldsen.lib.clients.LeagueClientApi;
import com.kjeldsen.lib.model.league.CreateOrAssignTeamToLeagueRequestClient;
import com.kjeldsen.lib.model.league.CreateOrAssignTeamToLeagueResponseClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;


@Component
@Slf4j
public class LeagueClientApiImpl extends BaseClientApiImpl implements LeagueClientApi {

    public LeagueClientApiImpl(WebClient webClient) {
        super(webClient);
    }

    @Override
    public CreateOrAssignTeamToLeagueResponseClient assignTeamToLeague(CreateOrAssignTeamToLeagueRequestClient request) {
        String uri = buildUri();
        return executePostRequest(uri, request,
            new ParameterizedTypeReference<>() {});
    }

    @Override
    protected String buildUri(String... params) {
        return UriComponentsBuilder.fromPath("/league")
            .toUriString();
    }
}
