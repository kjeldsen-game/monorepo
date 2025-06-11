package com.kjeldsen.integration.match;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjeldsen.integration.AbstractIT;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.persistence.mongo.repositories.MatchMongoRepository;
import com.kjeldsen.match.rest.model.*;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import com.kjeldsen.player.persistence.mongo.repositories.TeamMongoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Disabled

class MatchApiIT extends AbstractIT {

    @Autowired
    private PlayerReadRepository playerReadRepository;
    @Autowired
    private PlayerWriteRepository playerWriteRepository;
    @Autowired
    private PlayerMongoRepository playerMongoRepository;
    @Autowired
    private TeamMongoRepository teamMongoRepository;
    @Autowired
    private MatchMongoRepository matchMongoRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        playerMongoRepository.deleteAll();
        matchMongoRepository.deleteAll();
        teamMongoRepository.deleteAll();
    }

    @Nested
    @DisplayName("HTTP GET to /match should")
    class HttpGetToMatchShould {
        @Test
        @Disabled
        @DisplayName("return matches and 200")
        void return_matches_and_200() throws Exception {

            matchMongoRepository.save(Match.builder().id("matchId").build());
            matchMongoRepository.save(Match.builder().id("matchId2").build());

            MvcResult result = mockMvc.perform(get("/v1/match"))
                .andExpect(status().isOk())
                .andReturn();

            String jsonResponse = result.getResponse().getContentAsString();

            List<MatchResponse> matches = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

            assertThat(matches).isNotEmpty().hasSize(2);
            assertThat(matches.get(0).getId()).isEqualTo("matchId");
        }

        @Test
        @Disabled
        @DisplayName("return match by id  and 200")
        void return_match_by_id_and_200() throws Exception {

            matchMongoRepository.save(Match.builder().id("matchId").build());

            MvcResult result = mockMvc.perform(get("/v1/match/matchId"))
                .andExpect(status().isOk())
                .andReturn();

            String jsonResponse = result.getResponse().getContentAsString();
            MatchResponse match = objectMapper.readValue(jsonResponse, MatchResponse.class);
            assertThat(match.getId()).isEqualTo("matchId");
        }
    }

    @Nested
    @DisplayName("HTTP POST to /match should")
    class HttpPostToMatchShould {
        @Test
        @Disabled
        @DisplayName("return 201 and create a match ")
        void return_matches_and_200() throws Exception {

            teamMongoRepository.save(Team.builder().id(Team.TeamId.of("homeId")).build());
            teamMongoRepository.save(Team.builder().id(Team.TeamId.of("awayId")).build());

            CreateMatchRequest request = new CreateMatchRequest()
                .home(new CreateMatchRequestHome().id("homeId"))
                .away(new CreateMatchRequestHome().id("awayId"));

            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
//            JavaTimeModule javaTimeModule = new JavaTimeModule();
//            objectMapper.registerModule(javaTimeModule);
//            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            mockMvc.perform(post("/v1/match")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

            List<Match> matches = matchMongoRepository.findAll();
            assertThat(matches).isNotEmpty().hasSize(1);
            assertThat(matches.get(0).getHome().getId()).isEqualTo("homeId");
        }
    }

    @Nested
    @DisplayName("HTTP PATCH to /match/ should")
    class HttpPatchToMatchShould {
        @Test
        @DisplayName("return 200 and update match to ACCEPTED ")
        void update_match_and_return_200() throws Exception {
            matchMongoRepository.save(Match.builder().id("matchId").status(Match.Status.PENDING).build());

            EditMatchRequest request = new EditMatchRequest().status(Status.ACCEPTED);

            mockMvc.perform(patch("/v1/match/matchId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

            Optional<Match> result = matchMongoRepository.findById("matchId");
            if (result.isPresent()) {
                Match match = result.get();
                assertThat(match.getStatus()).isEqualTo(Match.Status.ACCEPTED);
            }
        }
    }
}
