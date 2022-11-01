package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjeldsen.player.Application;
import com.kjeldsen.player.application.usecases.CreatePlayerUseCase;
import com.kjeldsen.player.persistence.adapters.PlayerReadRepositoryAdapter;
import com.kjeldsen.player.persistence.adapters.PlayerWriteRepositoryAdapter;
import com.kjeldsen.player.persistence.mongo.documents.PlayerDocument;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import com.kjeldsen.player.rest.api.PlayerApiController;
import com.kjeldsen.player.rest.delegate.PlayerDelegate;
import com.kjeldsen.player.rest.model.CreatePlayerRequest;
import com.kjeldsen.player.rest.model.PlayerPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataMongo
@WebMvcTest(controllers = PlayerApiController.class)
@ContextConfiguration(classes = {Application.class})
@Import({PlayerDelegate.class, CreatePlayerUseCase.class, PlayerReadRepositoryAdapter.class, PlayerWriteRepositoryAdapter.class})
class PlayerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlayerMongoRepository playerMongoRepository;

    @BeforeEach
    void setUp() {
        playerMongoRepository.deleteAll();
    }

    @Nested
    @DisplayName("HTTP POST to /player should")
    class HttpPostToPlayerShould {
        @Test
        @DisplayName("return 201 when a valid request is sent")
        void return_201_status_when_a_valid_request_is_sent() throws Exception {
            CreatePlayerRequest request = new CreatePlayerRequest()
                .age(16)
                .position(PlayerPosition.FORWARD)
                .points(700);

            mockMvc.perform(post("/player")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

            var player = playerMongoRepository.findBy(Example.of(PlayerDocument.builder().age(16).build()), FetchableFluentQuery::one);

            assertThat(player.isPresent()).isTrue();
            assertThat(player.get().getAge()).isEqualTo(16);
            assertThat(player.get().getPosition()).isEqualTo("FORWARD");
        }
    }

}
