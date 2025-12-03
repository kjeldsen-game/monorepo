package com.kjeldsen.lib;

import com.kjeldsen.lib.model.player.PlayerClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerClientApiImplTest extends BaseClientApiTest {

    private PlayerClientApiImpl playerClientApi;

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        playerClientApi = new PlayerClientApiImpl(WebClient.builder().baseUrl(baseUrl).build());
    }

    @Test
    @DisplayName("Should get the Team from the client")
    void should_get_the_team_from_client() throws Exception {
        String jsonResponse = readJsonFile("players.json");
        mockWebServer.enqueue(new MockResponse()
            .setBody(jsonResponse)
            .addHeader("Content-Type", "application/json"));

        List<PlayerClient> result = playerClientApi.getPlayers("d1acbce5-ac43-423a-b402-0abc3941f51b", new ArrayList<>());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals(3, result.size());
        assertEquals("/player?teamId=d1acbce5-ac43-423a-b402-0abc3941f51b&size=100&page=0", recordedRequest.getPath());
    }

}