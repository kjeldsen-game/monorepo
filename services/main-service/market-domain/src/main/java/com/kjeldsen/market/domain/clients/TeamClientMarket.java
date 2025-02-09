package com.kjeldsen.market.domain.clients;

import com.kjeldsen.market.domain.clients.models.TeamDTO;

public interface TeamClientMarket {

    TeamDTO getTeam(String teamId, String token);

}
