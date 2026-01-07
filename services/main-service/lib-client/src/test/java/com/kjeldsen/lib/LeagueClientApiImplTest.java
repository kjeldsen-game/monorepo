package com.kjeldsen.lib;

import com.kjeldsen.match.rest.model.CreateOrAssignTeamToLeagueRequest;
import com.kjeldsen.match.rest.model.CreateOrAssignTeamToLeagueResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;


import static org.junit.jupiter.api.Assertions.*;

class LeagueClientApiImplTest extends BaseClientApiTest{

    private LeagueClientApiImpl leagueClientApi;

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        leagueClientApi = new LeagueClientApiImpl(WebClient.builder().baseUrl(baseUrl).build(), mockedInternalClientTokenProvider);
    }

    @Test
    @DisplayName("Should get the Team from the client")
    void should_get_the_team_from_client() throws Exception {
        mockWebServer.enqueue(new MockResponse()
            .setBody("{\"leagueId\": \"12435\"}")
            .addHeader("Content-Type", "application/json"));

        CreateOrAssignTeamToLeagueResponse result = leagueClientApi.
            assignTeamToLeague(new CreateOrAssignTeamToLeagueRequest()
            .teamName("team1")
            .teamId("teamId"));

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/league", recordedRequest.getPath());
        assertEquals("12435", result.getLeagueId());
    }
}