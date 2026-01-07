//package com.kjeldsen.lib;
//
//import com.kjeldsen.lib.model.team.TeamClient;
//import okhttp3.mockwebserver.MockResponse;
//import okhttp3.mockwebserver.RecordedRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.web.reactive.function.client.WebClient;
//
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class TeamClientApiImplTest extends BaseClientApiTest {
//
//    private TeamClientApiImpl teamClientApiImpl;
//
//    @BeforeEach
//    void initialize() {
//        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
//        teamClientApiImpl =new TeamClientApiImpl(WebClient.builder().baseUrl(baseUrl).build(), mockedInternalClientTokenProvider);
//    }
//
//    @Test
//    @DisplayName("Should get the Team from the client by id")
//    void should_get_the_team_from_client_by_id() throws Exception {
//        String jsonResponse = readJsonFile("team.json");
//
//        mockWebServer.enqueue(new MockResponse()
//            .setBody(jsonResponse)
//            .addHeader("Content-Type", "application/json"));
//
//        List<TeamClient> result = teamClientApiImpl.getTeam("d1acbce5-ac43-423a-b402-0abc3941f51b", null, null);
//        RecordedRequest recordedRequest = mockWebServer.takeRequest();
//        assertEquals("GET", recordedRequest.getMethod());
//        assertEquals("aa", result.get(0).getName());
//        assertEquals("/team/d1acbce5-ac43-423a-b402-0abc3941f51b", recordedRequest.getPath());
//
//    }
//
//    @Test
//    @DisplayName("Should get the Team from the client by team name")
//    void should_get_the_team_from_client_by_team_name() throws Exception {
//        String jsonResponse = readJsonFile("team.json");
//
//        mockWebServer.enqueue(new MockResponse()
//            .setBody(jsonResponse)
//            .addHeader("Content-Type", "application/json"));
//
//        List<TeamClient> result = teamClientApiImpl.getTeam(null, "aa", null);
//        System.out.println(result);
//        RecordedRequest recordedRequest = mockWebServer.takeRequest();
//        assertEquals("GET", recordedRequest.getMethod());
//        assertEquals("aa", result.get(0).getName());
//        assertEquals("/team?name=aa&size=10&page=0", recordedRequest.getPath());
//    }
//}