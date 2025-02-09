package com.kjeldsen.market.client;

import com.kjeldsen.market.domain.clients.TeamClientMarket;
import com.kjeldsen.market.domain.clients.models.TeamDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
@RequiredArgsConstructor
public class TeamClientImplMarket implements TeamClientMarket {

    private final WebClient webClient;

    @Override
    public TeamDTO getTeam(String teamId, String token) {

        return webClient.get()
            .uri("/team/{id}", teamId)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToMono(TeamDTO.class)
            .block();
    }
}
