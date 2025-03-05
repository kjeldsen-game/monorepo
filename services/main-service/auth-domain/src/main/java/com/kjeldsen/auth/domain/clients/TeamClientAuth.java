package com.kjeldsen.auth.domain.clients;

import com.kjeldsen.auth.domain.clients.models.TeamDTO;

import java.util.List;

public interface TeamClientAuth {
    List<TeamDTO> getTeam(String teamName, String userId);
}
