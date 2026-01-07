package com.kjeldsen.lib.clients;

import com.kjeldsen.match.rest.model.CreateOrAssignTeamToLeagueRequest;
import com.kjeldsen.match.rest.model.CreateOrAssignTeamToLeagueResponse;

public interface LeagueClientApi {
    CreateOrAssignTeamToLeagueResponse assignTeamToLeague(CreateOrAssignTeamToLeagueRequest request);
}
