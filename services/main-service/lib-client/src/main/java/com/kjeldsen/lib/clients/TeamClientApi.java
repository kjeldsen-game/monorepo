package com.kjeldsen.lib.clients;

import com.kjeldsen.lib.model.team.TeamClient;

import java.util.List;

public interface TeamClientApi {
    List<TeamClient> getTeam(String teamId, String teamName, String userId);
}
