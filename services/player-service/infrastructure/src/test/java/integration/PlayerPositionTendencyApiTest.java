package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjeldsen.player.PlayerServiceApplication;
import com.kjeldsen.player.application.usecases.UpdatePlayerPositionTendencyUseCase;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.persistence.adapters.mongo.PlayerPositionTendencyReadRepositoryMongoAdapter;
import com.kjeldsen.player.persistence.adapters.mongo.PlayerPositionTendencyWriteRepositoryMongoAdapter;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerPositionTendencyMongoRepository;
import com.kjeldsen.player.rest.api.PlayerPositionTendenciesApiController;
import com.kjeldsen.player.rest.delegate.PlayerPositionTendenciesDelegate;
import com.kjeldsen.player.rest.model.PlayerPosition;
import com.kjeldsen.player.rest.model.PlayerPositionTendencyResponse;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataMongo
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@WebMvcTest(controllers = PlayerPositionTendenciesApiController.class)
@ContextConfiguration(classes = {PlayerServiceApplication.class})
@Import({PlayerPositionTendenciesDelegate.class,
    UpdatePlayerPositionTendencyUseCase.class,
    PlayerPositionTendencyReadRepositoryMongoAdapter.class,
    PlayerPositionTendencyWriteRepositoryMongoAdapter.class})
public class PlayerPositionTendencyApiTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlayerPositionTendencyMongoRepository playerPositionTendencyStore;

    @BeforeEach
    public void setup() {
        playerPositionTendencyStore.deleteAll();
    }

    @Nested
    @DisplayName("HTTP GET to /player-position-tendencies should")
    class HttpGetToPlayerPositionTendencyShould {
        @Test
        @DisplayName("return a list of default and stored player position tendencies")
        public void return_a_list_player_position_tendencies() throws Exception {
            PlayerPositionTendency storedPlayerPositionTendency = PlayerPositionTendency.builder()
                .position(com.kjeldsen.player.domain.PlayerPosition.FORWARD)
                .tendencies(Map.of(PlayerSkill.SCORE, 5))
                .build();
            playerPositionTendencyStore.save(storedPlayerPositionTendency);

            List<PlayerPositionTendencyResponse> expected = List.of(
                new PlayerPositionTendencyResponse()
                    .position(PlayerPosition.FORWARD)
                    .tendencies(storedPlayerPositionTendency.getTendencies()
                        .entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().name(), entry -> entry.getValue().toString())))
                    ._default(false),
                new PlayerPositionTendencyResponse()
                    .position(PlayerPosition.DEFENDER)
                    .tendencies(PlayerPositionTendency.DEFAULT_DEFENDER_TENDENCIES.getTendencies()
                        .entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().name(), entry -> entry.getValue().toString())))
                    ._default(true),
                new PlayerPositionTendencyResponse()
                    .position(PlayerPosition.MIDDLE)
                    .tendencies(PlayerPositionTendency.DEFAULT_MIDDLE_TENDENCIES.getTendencies()
                        .entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().name(), entry -> entry.getValue().toString())))
                    ._default(true)
            );

            mockMvc.perform(get("/player-position-tendencies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
        }
    }

    @Nested
    @DisplayName("HTTP GET to /player-position-tendencies/{position} should")
    class HttpGetToPlayerPositionTendencyPositionShould {
        @Test
        @DisplayName("return a default player position tendency of a given position when no player position tendency exists in storage")
        public void return_a_default_player_position_tendency_of_a_given_position_when_no_player_position_tendency_exists_in_storage() throws Exception {
            PlayerPositionTendencyResponse expected = new PlayerPositionTendencyResponse()
                .position(PlayerPosition.DEFENDER)
                .tendencies(PlayerPositionTendency.DEFAULT_DEFENDER_TENDENCIES.getTendencies()
                    .entrySet().stream()
                    .collect(Collectors.toMap(entry -> entry.getKey().name(), entry -> entry.getValue().toString())))
                ._default(PlayerPositionTendency.DEFAULT_DEFENDER_TENDENCIES.isDefault());

            mockMvc.perform(get("/player-position-tendencies/DEFENDER"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
        }

        @Test
        @DisplayName("return a stored player position tendency of a given position when player position tendency exists in storage")
        public void return_a_stored_player_position_tendency_of_a_given_position_when_player_position_tendency_exists_in_storage() throws Exception {
            PlayerPositionTendency storedPlayerPositionTendency = PlayerPositionTendency.builder()
                .position(com.kjeldsen.player.domain.PlayerPosition.FORWARD)
                .tendencies(Map.of(PlayerSkill.SCORE, 5))
                .build();
            playerPositionTendencyStore.save(storedPlayerPositionTendency);

            PlayerPositionTendencyResponse expected = new PlayerPositionTendencyResponse()
                .position(PlayerPosition.FORWARD)
                .tendencies(storedPlayerPositionTendency.getTendencies()
                    .entrySet().stream()
                    .collect(Collectors.toMap(entry -> entry.getKey().name(), entry -> entry.getValue().toString())))
                ._default(false);

            mockMvc.perform(get("/player-position-tendencies/FORWARD"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
        }
    }

    @Nested
    @DisplayName("HTTP PATCH to /player-position-tendencies/{position} should")
    class HttpPatchToPlayerPositionTendencyPositionShould {
        @Test
        @DisplayName("return an updated player position tendency of a given position")
        public void return_an_updated_player_position_tendency_of_a_given_position() throws Exception {

            Map<PlayerSkill, String> request = Map.of(PlayerSkill.SCORE, "5");

            PlayerPositionTendencyResponse expected = new PlayerPositionTendencyResponse()
                .position(PlayerPosition.FORWARD)
                .tendencies(Map.of(PlayerSkill.SCORE.name(), "5"))
                ._default(false);

            mockMvc.perform(patch("/player-position-tendencies/FORWARD")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
        }
    }
}
