package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventReadRepository;
import com.kjeldsen.player.persistence.common.AbstractMongoDbTest;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerTrainingScheduledEventMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import java.util.List;

@DataMongoTest(includeFilters = @ComponentScan.Filter(classes = Component.class), excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@ActiveProfiles("test")
class PlayerTrainingScheduledEventReadRepositoryMongoAdapterIT extends AbstractMongoDbTest {

    @Autowired
    private PlayerTrainingScheduledEventMongoRepository playerTrainingScheduledEventMongoRepository;

    @Autowired
    private PlayerTrainingScheduledEventReadRepository playerTrainingScheduledEventReadRepository;

    @BeforeEach
    public void setup() {
        playerTrainingScheduledEventMongoRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return active scheduled trainings")
    public void should_return_active_scheduled_trainings() {
        PlayerTrainingScheduledEvent event = PlayerTrainingScheduledEvent
            .builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(Player.PlayerId.of("exampleId"))
            .skill(PlayerSkill.SCORING)
            .status(PlayerTrainingScheduledEvent.Status.ACTIVE)
            .build();

        PlayerTrainingScheduledEvent event2 = PlayerTrainingScheduledEvent
            .builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(Player.PlayerId.of("exampleId"))
            .skill(PlayerSkill.SCORING)
            .status(PlayerTrainingScheduledEvent.Status.INACTIVE)
            .build();
        playerTrainingScheduledEventMongoRepository.save(event);

        List<PlayerTrainingScheduledEvent> eventList = playerTrainingScheduledEventReadRepository
            .findAllActiveScheduledTrainings();

        assertThat(eventList.size()).isEqualTo(1);
        assertThat(eventList.get(0).getId()).isEqualTo(event.getId());
        assertThat(eventList.get(0).getSkill()).isEqualTo(event.getSkill());
    }
}