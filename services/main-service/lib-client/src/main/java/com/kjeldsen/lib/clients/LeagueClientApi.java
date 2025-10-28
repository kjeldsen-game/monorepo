package com.kjeldsen.lib.clients;

import com.kjeldsen.lib.model.league.CreateOrAssignTeamToLeagueRequestClient;
import com.kjeldsen.lib.model.league.CreateOrAssignTeamToLeagueResponseClient;

public interface LeagueClientApi {
    CreateOrAssignTeamToLeagueResponseClient assignTeamToLeague(CreateOrAssignTeamToLeagueRequestClient request);
}
