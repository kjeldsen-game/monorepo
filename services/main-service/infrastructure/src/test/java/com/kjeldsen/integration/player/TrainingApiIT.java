package com.kjeldsen.integration.player;

import com.kjeldsen.integration.AbstractIT;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import com.kjeldsen.player.rest.model.RegisterBloomPhaseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingApiIT extends AbstractIT {
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
    @DisplayName("HTTP POST to /training/{playerId}/bloom should")
    class HttpPostToTrainingPlayerShouldDo {
        @Test
        @DisplayName("return 201 /training/{playerId}/bloom register a new bloom phase")
        void return_201_status_when_a_valid_bloom_request_is_sent() throws Exception {
            RegisterBloomPhaseRequest request = new RegisterBloomPhaseRequest()
                .yearsOn(3)
                .bloomStartAge(18)
                .bloomSpeed(350);

            String playerId = "playerId1";

            playerWriteRepository.save(Player.builder()
                .id(Player.PlayerId.of(playerId))
                .age(18)
                .build());

            mockMvc.perform(post("/training/{playerId}/bloom", playerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

            Player player = playerReadRepository.findOneById(Player.PlayerId.of(playerId)).orElseThrow();

            assertThat(player.getBloom().getPlayerId().value()).isEqualTo(playerId);
            assertThat(player.getBloom().getBloomSpeed()).isEqualTo(350);
            assertThat(player.getBloom().getBloomStartAge()).isEqualTo(18);
            assertThat(player.getBloom().getYearsOn()).isEqualTo(3);
        }
    }
}
