package com.kjeldsen.match.domain.clients;

import com.kjeldsen.match.domain.clients.models.team.TeamDTO;

public interface TeamClientMatch {
    TeamDTO getTeam(String teamId, String token);
}
