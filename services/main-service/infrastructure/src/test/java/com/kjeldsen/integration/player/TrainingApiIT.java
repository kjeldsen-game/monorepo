package com.kjeldsen.integration.player;

import com.kjeldsen.integration.AbstractIT;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerCategory;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventReadRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerTrainingScheduledEventMongoRepository;
import com.kjeldsen.player.persistence.mongo.repositories.TeamMongoRepository;
import com.kjeldsen.player.rest.model.PlayerSkill;
import com.kjeldsen.player.rest.model.SchedulePlayerTrainingRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingApiIT extends AbstractIT {

    @Autowired
    private PlayerTrainingScheduledEventReadRepository playerTrainingScheduledEventReadRepository;
    @Autowired
    private PlayerTrainingScheduledEventMongoRepository playerTrainingScheduledEventMongoRepository;
    @Autowired
    private PlayerMongoRepository playerMongoRepository;
    @Autowired
    private TeamMongoRepository teamMongoRepository;

    @BeforeEach
    void setUp() {
        playerTrainingScheduledEventMongoRepository.deleteAll();
        playerMongoRepository.deleteAll();
        teamMongoRepository.deleteAll();
    }

    @Nested
    @DisplayName("HTTP POST to /v1/training/{playerId}")
    class HttpPostToTrainingShould {
        @Test
        @DisplayName("Should schedule the player training and return 200")
        public void should_return_200_when_training_is_scheduled() throws Exception {
            Team.TeamId teamId = new Team.TeamId("team1");
            teamMongoRepository.save(Team.builder().id(teamId).build());
            Player examplePlayer = PlayerProvider.generate(teamId, PlayerPositionTendency.DEFAULT_CENTRE_BACK_TENDENCIES,
                PlayerCategory.JUNIOR, 200);
            playerMongoRepository.save(examplePlayer);
            SchedulePlayerTrainingRequest request = new SchedulePlayerTrainingRequest()
                .skill(PlayerSkill.AERIAL);

            mockMvc.perform(post("/v1/training/{playerId}", examplePlayer.getId().value())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

            List<PlayerTrainingScheduledEvent> events = playerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings();
            assertFalse(events.isEmpty());
            assertEquals(examplePlayer.getId(), events.get(0).getPlayerId());
        }
    }
}
