package com.kjeldsen.lib;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjeldsen.lib.common.BaseClientApiTest;
import com.kjeldsen.player.rest.model.PlayerResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerClientApiImplTest extends BaseClientApiTest {

    private PlayerClientApiImpl playerClientApi;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        playerClientApi = new PlayerClientApiImpl(WebClient.builder().baseUrl(baseUrl).build(), mockedInternalClientTokenProvider);
    }

    @Test
    @DisplayName("Should get the players from the team")
    void should_get_players_from_team() throws Exception {
        String jsonResponse = readJsonFile("players.json");
        mockWebServer.enqueue(new MockResponse()
            .setBody(jsonResponse)
            .addHeader("Content-Type", "application/json"));

        List<PlayerResponse> result = playerClientApi.getPlayers("d1acbce5-ac43-423a-b402-0abc3941f51b");

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals(3, result.size());
        assertEquals("/player?teamId=d1acbce5-ac43-423a-b402-0abc3941f51b&size=50&page=0", recordedRequest.getPath());
    }

    @Test
    @DisplayName("Should get the player by id")
    void should_get_player_by_player_id() throws Exception {
        String jsonResponse = readJsonFile("players.json");
        String firstPlayerJson = objectMapper.writeValueAsString(objectMapper.readTree(jsonResponse).get(0));
        mockWebServer.enqueue(new MockResponse()
            .setBody(firstPlayerJson)
            .addHeader("Content-Type", "application/json"));

        PlayerResponse result = playerClientApi.getPlayer("9fe66f76-9867-406d-9216-c11623f0ebca");

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/player/9fe66f76-9867-406d-9216-c11623f0ebca", recordedRequest.getPath());
    }

}