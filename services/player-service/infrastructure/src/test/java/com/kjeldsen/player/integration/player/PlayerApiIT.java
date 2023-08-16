package com.kjeldsen.player.integration.player;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerCategory;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.FindPlayersQuery;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.integration.AbstractIT;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import com.kjeldsen.player.rest.model.CreatePlayerRequest;
import com.kjeldsen.player.rest.model.GeneratePlayersRequest;
import com.kjeldsen.player.rest.model.PlayerPosition;
import com.kjeldsen.player.rest.model.PlayerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PlayerApiIT extends AbstractIT {

    @Autowired
    private PlayerReadRepository playerReadRepository;
    @Autowired
    private PlayerWriteRepository playerWriteRepository;
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

            var players = playerReadRepository.find(findPlayersQuery(com.kjeldsen.player.domain.PlayerPosition.FORWARD));

            assertThat(players).hasSize(1);
            assertThat(players.get(0).getAge()).isEqualTo(16);
            assertThat(players.get(0).getPosition().name()).isEqualTo("FORWARD");
        }
    }

    @Nested
    @DisplayName("HTTP POST to /player/generate should")
    class HttpPostToPlayerGenerateShould {
        @Test
        @DisplayName("return 201 and the list of created players when a valid request is sent")
        void return_201_and_the_list_of_created_players_when_a_valid_request_is_sent() throws Exception {
            GeneratePlayersRequest request = new GeneratePlayersRequest()
                .numberOfPlayers(10);

            mockMvc.perform(post("/player/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.*", hasSize(10)));

            var numbersOfPlayersCreated = playerReadRepository.find(findPlayersQuery(null));

            assertThat(numbersOfPlayersCreated).hasSize(10);
        }
    }

    @Nested
    @DisplayName("HTTP GET to /player should")
    class HttpGetToPlayerShould {
        @Test
        @DisplayName("return a page of players")
        void return_a_page_of_players() throws Exception {
            IntStream.range(0, 100)
                .mapToObj(i -> PlayerPositionTendency.getDefault(PlayerProvider.position()))
                .map(positionTendencies -> PlayerProvider.generate(Team.TeamId.generate(), positionTendencies, PlayerCategory.JUNIOR, 200))
                .forEach(player -> playerWriteRepository.save(player));

            List<PlayerResponse> expected = playerReadRepository.find(findPlayersQuery(com.kjeldsen.player.domain.PlayerPosition.FORWARD))
                .stream()
                .sorted(Comparator.comparing(o -> o.getId().value()))
                .filter(player -> player.getPosition().name().equals("FORWARD"))
                .map(player ->
                    new PlayerResponse()
                        .id(player.getId().value())
                        .name(player.getName())
                        .age(player.getAge())
                        .position(PlayerPosition.fromValue(player.getPosition().name()))
                        .actualSkills(player.getActualSkills().entrySet().stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().name(), entry -> entry.getValue().toString()))
                        ))
                .toList()
                .subList(0, 10);

            mockMvc.perform(get("/player")
                    .queryParam("page", "0")
                    .queryParam("size", "10")
                    .queryParam("position", "FORWARD")
                    .queryParam("category", "JUNIOR")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
        }
    }

    @Nested
    @DisplayName("HTTP GET to /player/{playerId} should")
    class HttpGetToPlayerWithSpecificPlayerIdShould {
        @Test
        @DisplayName("return 200 and the specific player when a valid playerId is sent")
        void return_200_and_the_specific_player_when_a_valid_request_is_sent() throws Exception {
            Player examplePlayer = PlayerProvider.generate(Team.TeamId.generate(), PlayerPositionTendency.DEFAULT_DEFENDER_TENDENCIES, PlayerCategory.JUNIOR, 200);
            examplePlayer = playerWriteRepository.save(examplePlayer);

            PlayerResponse response = new PlayerResponse()
                .id(examplePlayer.getId().value())
                .name(examplePlayer.getName())
                .age(examplePlayer.getAge())
                .position(PlayerPosition.fromValue(examplePlayer.getPosition().name()))
                .category(com.kjeldsen.player.rest.model.PlayerCategory.fromValue(examplePlayer.getPlayerCategory().name()))
                .actualSkills(examplePlayer.getActualSkills().entrySet().stream()
                    .collect(Collectors.toMap(entry -> entry.getKey().name(), entry -> entry.getValue().toString()))
                );

            mockMvc.perform(get("/player/{playerId}", examplePlayer.getId().value()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
        }
    }

    private FindPlayersQuery findPlayersQuery(com.kjeldsen.player.domain.PlayerPosition position) {
        return FindPlayersQuery.builder()
            .page(0)
            .size(10)
            .position(position)
            .build();
    }

}
