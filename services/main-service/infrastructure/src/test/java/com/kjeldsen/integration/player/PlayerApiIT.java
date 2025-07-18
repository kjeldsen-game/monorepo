package com.kjeldsen.integration.player;

import com.kjeldsen.integration.AbstractIT;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerCategory;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.queries.FindPlayersQuery;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import com.kjeldsen.player.rest.mapper.PlayerMapper;
import com.kjeldsen.player.rest.model.CreatePlayerRequest;
import com.kjeldsen.player.rest.model.GeneratePlayersRequest;
import com.kjeldsen.player.rest.model.PlayerEconomy;
import com.kjeldsen.player.rest.model.PlayerOrder;
import com.kjeldsen.player.rest.model.PlayerPosition;
import com.kjeldsen.player.rest.model.PlayerResponse;
import com.kjeldsen.player.rest.model.PlayerResponseActualSkillsValue;
import com.kjeldsen.player.rest.model.PlayerStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        @Disabled
        @DisplayName("return 201 when a valid request is sent")
        void return_201_status_when_a_valid_request_is_sent() throws Exception {
            CreatePlayerRequest request = new CreatePlayerRequest()
                .position(PlayerPosition.FORWARD)
                .points(700);

            mockMvc.perform(post("/v1/player")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

            var players = playerReadRepository.find(findPlayersQuery(com.kjeldsen.player.domain.PlayerPosition.FORWARD));

            assertThat(players).hasSize(1);
            assertThat(players.get(0).getAge().getYears()).isNotNull();
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

            mockMvc.perform(post("/v1/player/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.*", hasSize(10)));

            var numbersOfPlayersCreated = playerReadRepository.findAll();

            assertThat(numbersOfPlayersCreated).hasSize(10);
        }
    }

    @Nested
    @DisplayName("HTTP GET to /player should")
    class HttpGetToPlayerShould {
        @Test
        @DisplayName("return a page of players")
        @Disabled
        void return_a_page_of_players() throws Exception {

            IntStream.range(0, 100)
                .mapToObj(i -> PlayerPositionTendency.getDefault(PlayerProvider.position()))
                .map(positionTendencies -> PlayerProvider.generate(Team.TeamId.generate(), positionTendencies, PlayerCategory.JUNIOR, 200))
                .forEach(player -> {
                    com.kjeldsen.player.domain.PlayerAge ageToCreate = com.kjeldsen.player.domain.PlayerAge.generateAgeOfAPlayer();
                    player.setAge(ageToCreate);
                    playerWriteRepository.save(player);
                });

            List<PlayerResponse> expected = playerReadRepository.find(findPlayersQuery(com.kjeldsen.player.domain.PlayerPosition.FORWARD))
                .stream()
                .sorted(Comparator.comparing(o -> o.getId().value()))
                .filter(player -> player.getPosition().name().equals("FORWARD"))
                .map(player ->
                    new PlayerResponse()
                        .id(player.getId().value())
                        .name(player.getName())
                        .age(PlayerMapper.INSTANCE.map(player.getAge()))
                        .category(com.kjeldsen.player.rest.model.PlayerCategory.valueOf(player.getCategory().name()))
                        .playerOrder(PlayerOrder.fromValue(player.getPlayerOrder().name()))
                        .economy(new PlayerEconomy().salary(player.getEconomy().getSalary().doubleValue()))
                        .status(PlayerStatus.fromValue(player.getStatus().name()))
                        .position(PlayerPosition.fromValue(player.getPosition().name()))
                        .actualSkills(player.getActualSkills().entrySet().stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerApiIT::map))
                        ))
                .toList();

            mockMvc.perform(get("/v1/player")
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

            Player examplePlayer = PlayerProvider.generate(Team.TeamId.of("exampleTeamId"), PlayerPositionTendency.DEFAULT_CENTRE_BACK_TENDENCIES,
                PlayerCategory.JUNIOR, 200);
            Player.Economy playerEconomy = examplePlayer.getEconomy();
            examplePlayer = playerWriteRepository.save(examplePlayer);

            PlayerResponse response = new PlayerResponse()
                .id(examplePlayer.getId().value())
                .teamId(examplePlayer.getTeamId().value())
                .name(examplePlayer.getName())
                .economy(map(playerEconomy))
                .status(PlayerStatus.INACTIVE)
                .age(PlayerMapper.INSTANCE.map(examplePlayer.getAge()))
                .playerOrder(PlayerOrder.NONE)
                .position(PlayerPosition.fromValue(examplePlayer.getPosition().name()))
                .category(com.kjeldsen.player.rest.model.PlayerCategory.fromValue(examplePlayer.getCategory().name()))
                .actualSkills(examplePlayer.getActualSkills().entrySet().stream()
                    .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerApiIT::map))
                );

            mockMvc.perform(get("/v1/player/{playerId}", examplePlayer.getId().value()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
        }
    }

    private FindPlayersQuery findPlayersQuery(com.kjeldsen.player.domain.PlayerPosition position) {
        return FindPlayersQuery.builder()
            .teamId(null)
            .page(0)
            .size(10)
            .position(position)
            .build();
    }

    public static PlayerResponseActualSkillsValue map(
        Map.Entry<com.kjeldsen.player.domain.PlayerSkill, com.kjeldsen.player.domain.PlayerSkills> mapEntrySkills) {
        PlayerResponseActualSkillsValue PlayerResponseActualSkillsValue = new PlayerResponseActualSkillsValue();
        PlayerResponseActualSkillsValue.setPlayerSkills(PlayerMapper.INSTANCE.map(mapEntrySkills.getValue()));
        return PlayerResponseActualSkillsValue;
    }

    public static PlayerEconomy map(Player.Economy economy) {
        PlayerEconomy playerEconomy = new PlayerEconomy();
        playerEconomy.setSalary(economy.getSalary().doubleValue());
        return playerEconomy;
    }
}
