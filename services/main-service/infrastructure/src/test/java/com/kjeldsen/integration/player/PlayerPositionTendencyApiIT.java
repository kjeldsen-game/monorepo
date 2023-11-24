package com.kjeldsen.integration.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjeldsen.integration.AbstractIT;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerPositionTendencyMongoRepository;
import com.kjeldsen.player.rest.mapper.PlayerMapper;
// import com.kjeldsen.player.rest.model.PlayerPosition; // TODO DALI ESTO ME ESTABA DANDO ERROR Y REQUERÍA UN TIPO REST EN PLAYERPOSITION Y TENÍA UN DOMAIN Y NO SE PORQUE
import com.kjeldsen.player.rest.model.PlayerPositionTendencyResponse;
import com.kjeldsen.player.rest.model.PlayerPositionTendencyResponseTendenciesValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlayerPositionTendencyApiIT extends AbstractIT {

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
                .tendencies(Map.of(PlayerSkill.SCORE, new PlayerSkills(7, 0)))
                .build();
            playerPositionTendencyStore.save(storedPlayerPositionTendency);

            List<PlayerPositionTendencyResponse> expected = List.of(
                new PlayerPositionTendencyResponse()
                    .position(com.kjeldsen.player.rest.model.PlayerPosition.CENTRE_BACK)
                    .tendencies(PlayerPositionTendency.DEFAULT_CENTRE_BACK_TENDENCIES.getTendencies()
                        .entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
                    ._default(true),

                new PlayerPositionTendencyResponse()
                    .position(com.kjeldsen.player.rest.model.PlayerPosition.SWEEPER)
                    .tendencies(PlayerPositionTendency.DEFAULT_SWEEPER_TENDENCIES.getTendencies()
                        .entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
                    ._default(true),

                new PlayerPositionTendencyResponse()
                    .position(com.kjeldsen.player.rest.model.PlayerPosition.LEFT_BACK)
                    .tendencies(PlayerPositionTendency.DEFAULT_LEFT_BACK_TENDENCIES.getTendencies()
                        .entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
                    ._default(true),

                new PlayerPositionTendencyResponse()
                    .position(com.kjeldsen.player.rest.model.PlayerPosition.RIGHT_BACK)
                    .tendencies(PlayerPositionTendency.DEFAULT_RIGHT_BACK_TENDENCIES.getTendencies()
                        .entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
                    ._default(true),

                new PlayerPositionTendencyResponse()
                    .position(com.kjeldsen.player.rest.model.PlayerPosition.LEFT_WINGBACK)
                    .tendencies(PlayerPositionTendency.DEFAULT_LEFT_WINGBACK_TENDENCIES.getTendencies()
                        .entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
                    ._default(true),

                new PlayerPositionTendencyResponse()
                    .position(com.kjeldsen.player.rest.model.PlayerPosition.RIGHT_WINGBACK)
                    .tendencies(PlayerPositionTendency.DEFAULT_RIGHT_WINGBACK_TENDENCIES.getTendencies()
                        .entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
                    ._default(true),

                new PlayerPositionTendencyResponse()
                    .position(com.kjeldsen.player.rest.model.PlayerPosition.DEFENSIVE_MIDFIELDER)
                    .tendencies(PlayerPositionTendency.DEFAULT_DEFENSIVE_MIDFIELDER_TENDENCIES.getTendencies()
                        .entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
                    ._default(true),

                /*new PlayerPositionTendencyResponse() // TODO MUDO COMPROBAR POR QUE ES EL UNICO DIFERENTE
                    .position(PlayerPosition.CENTRE_MIDFIELDER)
                    .tendencies(storedPlayerPositionTendency.getTendencies()
                        .entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
                    ._default(false),*/

                new PlayerPositionTendencyResponse()
                    .position(com.kjeldsen.player.rest.model.PlayerPosition.CENTRE_MIDFIELDER)
                    .tendencies(PlayerPositionTendency.DEFAULT_CENTRE_MIDFIELDER_TENDENCIES.getTendencies()
                        .entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
                    ._default(true),

                new PlayerPositionTendencyResponse()
                    .position(com.kjeldsen.player.rest.model.PlayerPosition.LEFT_MIDFIELDER)
                    .tendencies(PlayerPositionTendency.DEFAULT_LEFT_MIDFIELDER_TENDENCIES.getTendencies()
                            .entrySet().stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
                    ._default(true),

                new PlayerPositionTendencyResponse()
                    .position(com.kjeldsen.player.rest.model.PlayerPosition.RIGHT_MIDFIELDER)
                    .tendencies(PlayerPositionTendency.DEFAULT_RIGHT_MIDFIELDER_TENDENCIES.getTendencies()
                            .entrySet().stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
                    ._default(true),

                new PlayerPositionTendencyResponse()
                    .position(com.kjeldsen.player.rest.model.PlayerPosition.LEFT_WINGER)
                    .tendencies(PlayerPositionTendency.DEFAULT_LEFT_WINGER_TENDENCIES.getTendencies()
                            .entrySet().stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
                    ._default(true),

                new PlayerPositionTendencyResponse()
                    .position(com.kjeldsen.player.rest.model.PlayerPosition.OFFENSIVE_MIDFIELDER)
                    .tendencies(PlayerPositionTendency.DEFAULT_OFFENSIVE_MIDFIELDER_TENDENCIES.getTendencies()
                            .entrySet().stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
                    ._default(true),

                new PlayerPositionTendencyResponse()
                    .position(com.kjeldsen.player.rest.model.PlayerPosition.RIGHT_WINGER)
                    .tendencies(PlayerPositionTendency.DEFAULT_RIGHT_WINGER_TENDENCIES.getTendencies()
                            .entrySet().stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
                    ._default(true),

                new PlayerPositionTendencyResponse()
                    .position(com.kjeldsen.player.rest.model.PlayerPosition.FORWARD)
                    .tendencies(PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES.getTendencies()
                            .entrySet().stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
                    ._default(true),

                new PlayerPositionTendencyResponse()
                    .position(com.kjeldsen.player.rest.model.PlayerPosition.STRIKER)
                    .tendencies(PlayerPositionTendency.DEFAULT_STRIKER_TENDENCIES.getTendencies()
                            .entrySet().stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
                    ._default(true),

                new PlayerPositionTendencyResponse()
                    .position(com.kjeldsen.player.rest.model.PlayerPosition.GOALKEEPER)
                    .tendencies(PlayerPositionTendency.DEFAULT_GOALKEEPER_TENDENCIES.getTendencies()
                            .entrySet().stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
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
                .position(com.kjeldsen.player.rest.model.PlayerPosition.CENTRE_BACK)
                .tendencies(PlayerPositionTendency.DEFAULT_CENTRE_BACK_TENDENCIES.getTendencies()
                    .entrySet().stream()
                    .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
                ._default(PlayerPositionTendency.DEFAULT_CENTRE_BACK_TENDENCIES.isDefault());

            mockMvc.perform(get("/player-position-tendencies/CENTRE_BACK"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
        }

        @Test
        @DisplayName("return a stored player position tendency of a given position when player position tendency exists in storage")
        public void return_a_stored_player_position_tendency_of_a_given_position_when_player_position_tendency_exists_in_storage() throws Exception {
            PlayerPositionTendency storedPlayerPositionTendency = PlayerPositionTendency.builder()
                .position(com.kjeldsen.player.domain.PlayerPosition.FORWARD)
                .tendencies(Map.of(PlayerSkill.SCORE, new PlayerSkills(7, 0)))
                .build();
            playerPositionTendencyStore.save(storedPlayerPositionTendency);

            PlayerPositionTendencyResponse expected = new PlayerPositionTendencyResponse()
                .position(com.kjeldsen.player.rest.model.PlayerPosition.FORWARD)
                .tendencies(storedPlayerPositionTendency.getTendencies()
                    .entrySet().stream()
                    .collect(Collectors.toMap(entry -> entry.getKey().name(), PlayerPositionTendencyApiIT::map)))
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

            Map<PlayerSkill, Integer> request = Map.of(PlayerSkill.SCORE, 7);

            PlayerPositionTendencyResponseTendenciesValue var = new PlayerPositionTendencyResponseTendenciesValue();
            var.setPlayerSkills(new com.kjeldsen.player.rest.model.PlayerSkills().actual(7).potential(0));
            PlayerPositionTendencyResponse expected = new PlayerPositionTendencyResponse()
                .position(com.kjeldsen.player.rest.model.PlayerPosition.FORWARD)
                .tendencies(Map.of(PlayerSkill.SCORE.name(), var))
                ._default(false);

            mockMvc.perform(patch("/player-position-tendencies/FORWARD")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
        }
    }

    public static PlayerPositionTendencyResponseTendenciesValue map(Map.Entry<com.kjeldsen.player.domain.PlayerSkill, com.kjeldsen.player.domain.PlayerSkills> mapEntrySkills) {
        PlayerPositionTendencyResponseTendenciesValue playerPositionTendencyResponseTendenciesValue = new PlayerPositionTendencyResponseTendenciesValue();
        playerPositionTendencyResponseTendenciesValue.setPlayerSkill(PlayerMapper.INSTANCE.playerSkillMap(mapEntrySkills.getKey().name()));
        playerPositionTendencyResponseTendenciesValue.setPlayerSkills(PlayerMapper.INSTANCE.map(mapEntrySkills.getValue()));
        return playerPositionTendencyResponseTendenciesValue;
    }


}
