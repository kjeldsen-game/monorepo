package com.kjeldsen.match.client;

import com.kjeldsen.match.domain.clients.models.player.PlayerDTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerClientImplMatchTest extends ClientBaseTest{

    private PlayerClientImplMatch playerClientMatch;

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        playerClientMatch = new PlayerClientImplMatch(WebClient.builder().baseUrl(baseUrl).build());
    }

    @Test
    @DisplayName("Should get the Team from the client")
    void should_get_the_team_from_client() throws Exception {
        String jsonResponse = readJsonFile("players.json");

        mockWebServer.enqueue(new MockResponse()
            .setBody(jsonResponse)
            .addHeader("Content-Type", "application/json"));

        List<PlayerDTO> result = playerClientMatch.getPlayers("d1acbce5-ac43-423a-b402-0abc3941f51b", "token");

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals(3, result.size());
        assertEquals("/player?size=100&teamId=d1acbce5-ac43-423a-b402-0abc3941f51b", recordedRequest.getPath());
    }
}