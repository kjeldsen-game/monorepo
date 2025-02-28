package com.kjeldsen.match.client;

import com.kjeldsen.match.domain.clients.TeamClientMatch;
import com.kjeldsen.match.domain.clients.models.team.TeamDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class TeamClientImplMatch implements TeamClientMatch {

    private final WebClient webClient;

    @Override
    public TeamDTO getTeam(String teamId, String token) {
        try {
            return webClient.get()
                .uri("/team/{id}", teamId)
                .header("X-Internal-API-Key", "my-secret-key")
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                    response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            System.err.println("Server returned 500: " + errorBody);
                            return Mono.error(new RuntimeException("Server error: " + errorBody));
                        })
                )
                .bodyToMono(TeamDTO.class)
                .block();
        } catch (WebClientResponseException e) {
            System.err.println("WebClient Error: " + e.getStatusCode() + " " + e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            throw e;
        }
    }

}
