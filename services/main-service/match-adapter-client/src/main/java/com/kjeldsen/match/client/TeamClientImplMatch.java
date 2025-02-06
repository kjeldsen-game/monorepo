package com.kjeldsen.match.client;

import com.kjeldsen.match.domain.clients.TeamClientMatch;
import com.kjeldsen.match.domain.clients.models.team.TeamDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
@RequiredArgsConstructor
public class TeamClientImplMatch implements TeamClientMatch {

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
