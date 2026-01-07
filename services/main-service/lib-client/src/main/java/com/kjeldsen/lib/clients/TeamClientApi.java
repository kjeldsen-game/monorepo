package com.kjeldsen.lib.clients;

import com.kjeldsen.player.rest.model.TeamResponse;

import java.util.List;

public interface TeamClientApi {
    List<TeamResponse> getTeams(String teamName, String userId);

    TeamResponse getTeamById(String teamId);
}
