package com.kjeldsen.integration.player;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kjeldsen.integration.AbstractIT;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import com.kjeldsen.player.rest.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class SimulatedApiIT extends AbstractIT {
    @Autowired
    private PlayerWriteRepository playerWriteRepository;
    @Autowired
    private PlayerMongoRepository playerMongoRepository;

    @BeforeEach
    void setUp() {
        playerMongoRepository.deleteAll();
    }

    @Nested
    @DisplayName("HTTP POST to /simulator/training/{playerId} should")
    class HttpPostToSimulatorTrainingShould {
        @Test
        @DisplayName("return 200 and the list of the registered simulated scheduled trainings")
        void return_200_and_the_list_of_the_registered_simulated_scheduled_trainings() throws Exception {

            List<PlayerSkill> skillList = new ArrayList<>();
            skillList.add(PlayerSkill.CONSTITUTION);
            skillList.add(PlayerSkill.SCORING);

            RegisterSimulatedScheduledTrainingRequest request = new RegisterSimulatedScheduledTrainingRequest()
                .days(2)
                .skills(skillList);

            String playerId = "playerId1";

            playerWriteRepository.save(Player.builder()
                .id(Player.PlayerId.of(playerId))
                .actualSkills(Map.of(
                    com.kjeldsen.player.domain.PlayerSkill.CONSTITUTION, new com.kjeldsen.player.domain.PlayerSkills(1, 0, com.kjeldsen.player.domain.PlayerSkillRelevance.SECONDARY),
                    com.kjeldsen.player.domain.PlayerSkill.SCORING, new com.kjeldsen.player.domain.PlayerSkills(2, 0, com.kjeldsen.player.domain.PlayerSkillRelevance.SECONDARY)
                ))
                .build());

            MvcResult mvcResult = mockMvc.perform(post("/simulator/training/" + playerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

            PlayerHistoricalTrainingResponse playerHistoricalTrainingResponse1 = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), PlayerHistoricalTrainingResponse.class);

            assertThat(playerHistoricalTrainingResponse1.getPlayerId()).isEqualTo(playerId);
            assertThat(playerHistoricalTrainingResponse1.getTrainings().size()).isEqualTo(4);

            assertThat(playerHistoricalTrainingResponse1.getTrainings().get(0).getPlayerId()).isEqualTo(playerId);
            assertThat(playerHistoricalTrainingResponse1.getTrainings().get(0).getCurrentDay()).isEqualTo(1);
            assertThat(playerHistoricalTrainingResponse1.getTrainings().get(0).getSkill()).isEqualTo(PlayerSkill.CONSTITUTION);

            assertThat(playerHistoricalTrainingResponse1.getTrainings().get(1).getPlayerId()).isEqualTo(playerId);
            assertThat(playerHistoricalTrainingResponse1.getTrainings().get(1).getCurrentDay()).isEqualTo(2);
            assertThat(playerHistoricalTrainingResponse1.getTrainings().get(1).getSkill()).isEqualTo(PlayerSkill.CONSTITUTION);

            assertThat(playerHistoricalTrainingResponse1.getTrainings().get(2).getPlayerId()).isEqualTo(playerId);
            assertThat(playerHistoricalTrainingResponse1.getTrainings().get(2).getCurrentDay()).isEqualTo(1);
            assertThat(playerHistoricalTrainingResponse1.getTrainings().get(2).getSkill()).isEqualTo(PlayerSkill.SCORING);

            assertThat(playerHistoricalTrainingResponse1.getTrainings().get(3).getPlayerId()).isEqualTo(playerId);
            assertThat(playerHistoricalTrainingResponse1.getTrainings().get(3).getCurrentDay()).isEqualTo(2);
            assertThat(playerHistoricalTrainingResponse1.getTrainings().get(3).getSkill()).isEqualTo(PlayerSkill.SCORING);

        }
    }

    @Nested
    @DisplayName("HTTP POST to /simulator/decline/{playerId} should")
    class HttpPostToSimulatorDeclineTrainingShould {
        @Test
        @DisplayName("return 200 and the list of the registered simulated decline trainings")
        void return_200_and_the_list_of_the_registered_simulated_decline_scheduled_trainings() throws Exception {

            RegisterSimulatedDeclineRequest request = new RegisterSimulatedDeclineRequest()
                .daysToDecline(15)
                .declineSpeed(100);

            Player player = playerWriteRepository.save(PlayerProvider.generateDefault());


            MvcResult mvcResult = mockMvc.perform(post("/simulator/decline/" + player.getId().value())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

            List<PlayerDeclineResponse> playerDeclineResponsesList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertThat(playerDeclineResponsesList.get(0).getPlayerId()).isEqualTo(player.getId().value());
            assertThat(playerDeclineResponsesList.size()).isEqualTo(15);

        }
    }
}
