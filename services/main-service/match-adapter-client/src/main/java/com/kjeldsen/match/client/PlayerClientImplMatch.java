package com.kjeldsen.match.client;

import com.kjeldsen.match.domain.clients.PlayerClientMatch;
import com.kjeldsen.match.domain.clients.models.player.PlayerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PlayerClientImplMatch implements PlayerClientMatch {

    private final WebClient webClient;

    public List<PlayerDTO> getPlayers(String teamId, String token) {

        return webClient.get()
            .uri(teamId != null ? "/player?size=100&teamId=" + teamId : "/player?size=100")
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<PlayerDTO>>() {})
            .block();
    }
}
