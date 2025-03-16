package com.kjeldsen.auth.client;

import com.kjeldsen.auth.domain.clients.TeamClientAuth;
import com.kjeldsen.auth.domain.clients.models.TeamDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TeamClientImplAuth implements TeamClientAuth {

    private final WebClient webClient;

    @Override
    public List<TeamDTO> getTeam(String teamName, String userId) {
        String uri = buildUri(teamName, userId);

        return webClient.get()
            .uri(uri)
            .header("X-Internal-API-Key", "my-secret-key")
            .retrieve()
            .onStatus(HttpStatusCode::is5xxServerError, response ->
                response.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(new RuntimeException("Server error: " + errorBody)))
            )
            .bodyToMono(new ParameterizedTypeReference<List<TeamDTO>>() {})
            .onErrorResume(e -> Mono.empty())
            .block();
    }



    private String buildUri(String teamName, String userId) {
        return UriComponentsBuilder.fromPath("/team")
            .queryParamIfPresent("name", teamName != null ? Optional.of(teamName) : Optional.empty())
            .queryParamIfPresent("userId", userId != null ? Optional.of(userId) : Optional.empty())
            .queryParam("size", 10)
            .queryParam("page", 0)
            .toUriString();
    }
}
