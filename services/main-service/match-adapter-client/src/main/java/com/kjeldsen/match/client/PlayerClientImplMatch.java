package com.kjeldsen.match.client;

import com.kjeldsen.match.domain.clients.PlayerClientMatch;
import com.kjeldsen.match.domain.clients.models.player.PlayerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PlayerClientImplMatch implements PlayerClientMatch {

    private final WebClient webClient;

    public List<PlayerDTO> getPlayers(String teamId, String token) {
        String uri = teamId != null ? "/player?size=100&teamId=" + teamId : "/player?size=100";
        try {
            return webClient.get()
                .uri(uri)
                .header("X-Internal-API-Key", "my-secret-key")
                // .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<PlayerDTO>>() {})
                .block();
        } catch (WebClientResponseException e) {
            log.error("Error response from Player API: Status={}, Body={}, Message={}",
                e.getStatusCode(), e.getResponseBodyAsString(), e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while calling Player API: Message={}", e.getMessage(), e);
        }
        return List.of(); // Return an empty list in case of failure
    }
}
