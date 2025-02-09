package com.kjeldsen.match.client;

import com.kjeldsen.match.domain.clients.models.team.TeamDTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TeamClientImplMatchTest extends ClientBaseTest {

    private TeamClientImplMatch teamClientMatch;

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        teamClientMatch = new TeamClientImplMatch(WebClient.builder().baseUrl(baseUrl).build());
    }

    @Test
    @DisplayName("Should get the Team from the client")
    void should_get_the_team_from_client() throws Exception {
        String jsonResponse = readJsonFile("team.json");

        mockWebServer.enqueue(new MockResponse()
            .setBody(jsonResponse)
            .addHeader("Content-Type", "application/json"));

        TeamDTO result = teamClientMatch.getTeam("d1acbce5-ac43-423a-b402-0abc3941f51b", "token");

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("aa", result.getName());
        assertEquals("/team/d1acbce5-ac43-423a-b402-0abc3941f51b", recordedRequest.getPath());
    }
}