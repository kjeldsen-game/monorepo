package com.kjeldsen.player.persistence.adapters.mongo.training;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.models.training.TrainingEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.training.TrainingEventWriteRepository;
import com.kjeldsen.player.persistence.common.AbstractMongoDbTest;
import com.kjeldsen.player.persistence.mongo.repositories.training.TrainingEventMongoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@DataMongoTest(includeFilters = @ComponentScan.Filter(classes = Component.class))
@ActiveProfiles("test")
class TrainingEventWriteRepositoryMongoAdapterIT extends AbstractMongoDbTest {

    @Autowired
    private TrainingEventWriteRepository trainingEventWriteRepository;
    @Autowired
    private TrainingEventMongoRepository trainingEventMongoRepository;

    @BeforeEach
    void setup() {
        trainingEventMongoRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save TrainingEvent object")
    void should_save_TrainingEvent_object() {
        TrainingEvent event = TrainingEvent.builder()
            .type(TrainingEvent.TrainingType.DECLINE_TRAINING)
            .modifier(null)
            .occurredAt(InstantProvider.now())
            .player(Player.builder()
                .id(Player.PlayerId.of("id"))
                .name("name")
                .preferredPosition(PlayerPosition.GOALKEEPER)
                .build())
            .teamId(Team.TeamId.of("teamId"))
            .points(2)
            .skill(PlayerSkill.REFLEXES)
            .pointsAfterTraining(21)
            .pointsBeforeTraining(19)
            .build();
        TrainingEvent result = trainingEventWriteRepository.save(event);
        Assertions.assertEquals(event, result);
        System.out.println(result);
    }
}