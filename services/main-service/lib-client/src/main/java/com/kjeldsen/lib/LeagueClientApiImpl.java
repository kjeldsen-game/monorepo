package com.kjeldsen.lib;

import com.kjeldsen.lib.auth.InternalClientTokenProvider;
import com.kjeldsen.lib.clients.LeagueClientApi;
import com.kjeldsen.lib.clients.base.AuthenticatedClientApiImpl;
import com.kjeldsen.match.rest.model.CreateOrAssignTeamToLeagueRequest;
import com.kjeldsen.match.rest.model.CreateOrAssignTeamToLeagueResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;


@Component
@Slf4j
public class LeagueClientApiImpl extends AuthenticatedClientApiImpl implements LeagueClientApi {

    public LeagueClientApiImpl(WebClient webClient, InternalClientTokenProvider internalClientTokenProvider) {
        super(webClient, internalClientTokenProvider);
    }
    @Override
    public CreateOrAssignTeamToLeagueResponse assignTeamToLeague(CreateOrAssignTeamToLeagueRequest request) {
        String uri = UriComponentsBuilder.fromPath("/league").toUriString();
        return executeMono(() -> executeRequest(HttpMethod.POST, uri, null, request),
            new ParameterizedTypeReference<>() {});
    }
}
