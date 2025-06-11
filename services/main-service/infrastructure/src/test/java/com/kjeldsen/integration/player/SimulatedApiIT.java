package com.kjeldsen.integration.player;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.integration.AbstractIT;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerCategory;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.generator.PotentialRiseGenerator;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerPotentialRiseEventMongoRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerTrainingDeclineEventMongoRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerTrainingScheduledEventMongoRepository;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Disabled

class SimulatedApiIT extends AbstractIT {
    @Autowired
    private PlayerMongoRepository playerMongoRepository;
    @Autowired
    private PlayerTrainingScheduledEventMongoRepository playerTrainingScheduledEventMongoRepository;
    @Autowired
    private PlayerTrainingDeclineEventMongoRepository playerTrainingDeclineEventMongoRepository;
    @Autowired
    private PlayerPotentialRiseEventMongoRepository playerPotentialRiseEventMongoRepository;


    @BeforeEach
    void setUp() {
        playerMongoRepository.deleteAll();
        playerTrainingScheduledEventMongoRepository.deleteAll();
        playerTrainingDeclineEventMongoRepository.deleteAll();
        playerPotentialRiseEventMongoRepository.deleteAll();
    }

    @Nested
    @DisplayName("HTTP POST to /v1/simulator/")
    class HttpPost {

        @Test
        @DisplayName("Should process the scheduled trainings and return 200")
        void should_return_200_when_training_is_scheduled() throws Exception {
            Player examplePlayer = PlayerProvider.generate(Team.TeamId.generate(), PlayerPositionTendency.DEFAULT_CENTRE_BACK_TENDENCIES,
                PlayerCategory.JUNIOR, 200);
            playerMongoRepository.save(examplePlayer);

            playerTrainingScheduledEventMongoRepository.save(PlayerTrainingScheduledEvent.builder()
                .id(EventId.generate())
                .playerId(examplePlayer.getId()).
                skill(com.kjeldsen.player.domain.PlayerSkill.AERIAL)
                .status(PlayerTrainingScheduledEvent.Status.ACTIVE)
                .build());

            mockMvc.perform(post("/v1/simulator/trainings"))
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should process player declines and return 200")
        void should_return_200_when_decline_is_executed() throws Exception {
            Player examplePlayer = PlayerProvider.generate(Team.TeamId.generate(), PlayerPositionTendency.DEFAULT_CENTRE_BACK_TENDENCIES,
                PlayerCategory.JUNIOR, 200);
            examplePlayer.getAge().setYears(28);
            playerMongoRepository.save(examplePlayer);

            mockMvc.perform(post("/v1/simulator/player-declines"))
                .andExpect(status().isOk());

            List<PlayerTrainingDeclineEvent> events = playerTrainingDeclineEventMongoRepository.findAll();
            assertEquals(examplePlayer.getId(), events.get(0).getPlayerId());
            assertEquals(1, events.size());
        }

        @Test
        @DisplayName("Should process player potential rises and return 200")
        void should_return_200_when_process_potential_rises() throws Exception {
            Player examplePlayer1 = PlayerProvider.generate(Team.TeamId.generate(), PlayerPositionTendency.DEFAULT_CENTRE_BACK_TENDENCIES,
                PlayerCategory.JUNIOR, 200);
            examplePlayer1.getAge().setYears(28);
            Player examplePlayer2 = PlayerProvider.generate(Team.TeamId.generate(), PlayerPositionTendency.DEFAULT_CENTRE_BACK_TENDENCIES,
                PlayerCategory.JUNIOR, 200);
            examplePlayer2.getAge().setYears(20);
            playerMongoRepository.saveAll(List.of(examplePlayer1, examplePlayer2));

            try (MockedStatic<PotentialRiseGenerator> mockedStatic = mockStatic(PotentialRiseGenerator.class)) {
                mockedStatic.when(PotentialRiseGenerator::generatePotentialRaise).thenReturn(1);

                mockMvc.perform(post("/v1/simulator/potential-rises"))
                    .andExpect(status().isOk());
            }

            List<PlayerPotentialRiseEvent> events = playerPotentialRiseEventMongoRepository.findAll();
            assertEquals(1, events.size());
        }
    }
}
