package com.kjeldsen.player.integration.player;

import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.integration.AbstractIT;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import com.kjeldsen.player.rest.model.PlayerSkill;
import com.kjeldsen.player.rest.model.RegisterSimulatedScheduledTrainingRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class SimulatedApiIT extends AbstractIT {
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
    @DisplayName("HTTP POST to /simulator/training/{playerId} should")
    class HttpPostToSimulatorTrainingShould {
        @Test
        @DisplayName("return 200 and the list of the registered simulated scheduled trainings")
        void return_200_and_the_list_of_the_registered_simulated_scheduled_trainings() throws Exception {

            List<PlayerSkill> skillList = new ArrayList<>();
            skillList.add(PlayerSkill.CO);
            skillList.add(PlayerSkill.SCORE);

            RegisterSimulatedScheduledTrainingRequest registerSimulatedScheduledTrainingRequest1 = new RegisterSimulatedScheduledTrainingRequest()
                .days(4)
                .skills(skillList);


//
//            mockMvc.perform(post("/player/generate")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.*", hasSize(10)));

//            var numbersOfPlayersCreated = playerReadRepository.find(findPlayersQuery(null));

//            assertThat(numbersOfPlayersCreated).hasSize(10);
        }
    }
}
