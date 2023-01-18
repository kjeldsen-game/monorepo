package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjeldsen.player.PlayerServiceApplication;
import com.kjeldsen.player.application.usecases.CreatePlayerUseCase;
import com.kjeldsen.player.application.usecases.GeneratePlayersUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.persistence.adapters.cache.PlayerReadRepositoryCacheAdapter;
import com.kjeldsen.player.persistence.adapters.cache.PlayerWriteRepositoryCacheAdapter;
import com.kjeldsen.player.persistence.adapters.mongo.PlayerPositionTendencyReadRepositoryMongoAdapter;
import com.kjeldsen.player.persistence.adapters.mongo.PlayerPositionTendencyWriteRepositoryMongoAdapter;
import com.kjeldsen.player.persistence.cache.PlayerInMemoryCacheStore;
import com.kjeldsen.player.rest.api.PlayersApiController;
import com.kjeldsen.player.rest.delegate.PlayersDelegate;
import com.kjeldsen.player.rest.model.CreatePlayerRequest;
import com.kjeldsen.player.rest.model.GeneratePlayersRequest;
import com.kjeldsen.player.rest.model.PlayerPosition;
import com.kjeldsen.player.rest.model.PlayerResponse;
import common.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataMongo
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@WebMvcTest(controllers = PlayersApiController.class)
@ContextConfiguration(classes = {PlayerServiceApplication.class})
@Import({PlayersDelegate.class,
    CreatePlayerUseCase.class,
    GeneratePlayersUseCase.class,
    PlayerWriteRepositoryCacheAdapter.class,
    PlayerReadRepositoryCacheAdapter.class,
    PlayerPositionTendencyReadRepositoryMongoAdapter.class,
    PlayerPositionTendencyWriteRepositoryMongoAdapter.class,
    PlayerInMemoryCacheStore.class})
class PlayerApiTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlayerInMemoryCacheStore playerStore;

    @BeforeEach
    void setUp() {
        playerStore.clear();
    }

    @Nested
    @DisplayName("HTTP POST to /players should")
    class HttpPostToPlayerShould {
        @Test
        @DisplayName("return 201 when a valid request is sent")
        void return_201_status_when_a_valid_request_is_sent() throws Exception {
            CreatePlayerRequest request = new CreatePlayerRequest()
                .age(16)
                .position(PlayerPosition.FORWARD)
                .points(700);

            mockMvc.perform(post("/players")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

            var players = playerStore.getAll();

            assertThat(players).hasSize(1);
            assertThat(players.get(0).getAge().value()).isEqualTo(16);
            assertThat(players.get(0).getPosition().name()).isEqualTo("FORWARD");
        }
    }

    @Nested
    @DisplayName("HTTP POST to /players/generate should")
    class HttpPostToPlayerGenerateShould {
        @Test
        @DisplayName("return 201 and the list of created players when a valid request is sent")
        public void return_201_and_the_list_of_created_players_when_a_valid_request_is_sent() throws Exception {
            GeneratePlayersRequest request = new GeneratePlayersRequest()
                .numberOfPlayers(10);

            mockMvc.perform(post("/players/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.*", hasSize(10)));

            var numbersOfPlayersCreated = playerStore.size();

            assertThat(numbersOfPlayersCreated).isEqualTo(10);
        }
    }

    @Nested
    @DisplayName("HTTP GET to /players should")
    class HttpGetToPlayerShould {
        @Test
        @DisplayName("return a page of players")
        public void return_a_page_of_players() throws Exception {
            IntStream.range(0, 100)
                .mapToObj(i -> PlayerPositionTendency.getDefault(com.kjeldsen.player.domain.PlayerPosition.random()))
                .map(positionTendencies -> Player.generate(positionTendencies, 200))
                .forEach(player -> playerStore.put(player.getId().value(), player));

            List<PlayerResponse> expected = playerStore.getAll().stream()
                .sorted(Comparator.comparing(o -> o.getId().value()))
                .filter(player -> player.getPosition().name().equals("FORWARD"))
                .map(player ->
                    new PlayerResponse()
                        .id(UUID.fromString(player.getId().value()))
                        .name(player.getName().value())
                        .age(player.getAge().value())
                        .position(PlayerPosition.fromValue(player.getPosition().name()))
                        .actualSkills(player.getActualSkills().values().entrySet().stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().name(), entry -> entry.getValue().toString()))
                        ))
                .toList()
                .subList(0, 10);

            mockMvc.perform(get("/players")
                    .queryParam("page", "0")
                    .queryParam("size", "10")
                    .queryParam("position", "FORWARD"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
        }
    }

}
