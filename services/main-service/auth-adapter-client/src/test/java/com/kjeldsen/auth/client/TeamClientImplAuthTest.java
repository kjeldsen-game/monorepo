package com.kjeldsen.auth.client;

import com.kjeldsen.auth.domain.clients.models.TeamDTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeamClientImplAuthTest extends ClientBaseTest {


    private TeamClientImplAuth teamClientAuth;

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        teamClientAuth = new TeamClientImplAuth(WebClient.builder().baseUrl(baseUrl).build());
    }


    @Test
    @DisplayName("Should get the Team from the client by teamName")
    void should_get_the_team_from_client_by_teamName() throws Exception {
        String jsonResponse = readJsonFile("team.json");

        mockWebServer.enqueue(new MockResponse()
            .setBody(jsonResponse)
            .addHeader("Content-Type", "application/json"));

        List<TeamDTO> result = teamClientAuth.getTeam("aa", null);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("aa", result.get(0).getName());
        assertEquals("/team?name=aa&size=10&page=0", recordedRequest.getPath());
    }

    @Test
    @DisplayName("Should get the Team from the client by userId")
    void should_get_the_team_from_client_by_userId() throws Exception {
        String jsonResponse = readJsonFile("team.json");

        mockWebServer.enqueue(new MockResponse()
            .setBody(jsonResponse)
            .addHeader("Content-Type", "application/json"));

        List<TeamDTO> result = teamClientAuth.getTeam(null, "userId");

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("aa", result.get(0).getName());
        assertEquals("/team?userId=userId&size=10&page=0", recordedRequest.getPath());
    }
}