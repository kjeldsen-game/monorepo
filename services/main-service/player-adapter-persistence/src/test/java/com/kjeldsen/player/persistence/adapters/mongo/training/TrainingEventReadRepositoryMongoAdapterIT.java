package com.kjeldsen.player.persistence.adapters.mongo.training;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.models.training.TrainingEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.training.TrainingEventReadRepository;
import com.kjeldsen.player.persistence.common.AbstractMongoDbTest;
import com.kjeldsen.player.persistence.mongo.repositories.training.TrainingEventMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest(includeFilters = @ComponentScan.Filter(classes = Component.class))
@ActiveProfiles("test")
class TrainingEventReadRepositoryMongoAdapterIT extends AbstractMongoDbTest {

    @Autowired
    private TrainingEventReadRepository trainingEventReadRepository;
    @Autowired
    private TrainingEventMongoRepository trainingEventMongoRepository;

    @BeforeEach
    void setup() {
        trainingEventMongoRepository.deleteAll();
    }

    @Test
    @DisplayName("Should retrieve the data by ID")
    void should_retrieve_the_data_by_id() {
        List<TrainingEvent> events = new ArrayList<>();
        for (TrainingEvent.TrainingType type : TrainingEvent.TrainingType.values()) {
            events.add(
                TrainingEvent.builder()
                    .type(type)
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
                    .build()
            );
        }
        trainingEventMongoRepository.saveAll(events);
        List<TrainingEvent> results = trainingEventReadRepository.findAllSuccessfulByTeamIdTypeOccurredAt(Team.TeamId.of("teamId"),
            TrainingEvent.TrainingType.DECLINE_TRAINING, null);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getType()).isEqualTo(TrainingEvent.TrainingType.DECLINE_TRAINING);

        results = trainingEventReadRepository.findAllSuccessfulByTeamIdTypeOccurredAt(Team.TeamId.of("teamId"),
            TrainingEvent.TrainingType.POTENTIAL_RISE, null);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getType()).isEqualTo(TrainingEvent.TrainingType.POTENTIAL_RISE);

        results = trainingEventReadRepository.findAllSuccessfulByTeamIdTypeOccurredAt(Team.TeamId.of("teamId"),
            TrainingEvent.TrainingType.PLAYER_TRAINING, null);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getType()).isEqualTo(TrainingEvent.TrainingType.PLAYER_TRAINING);

        results = trainingEventReadRepository.findAllSuccessfulByTeamIdTypeOccurredAt(Team.TeamId.of("teamId"),
            null, null);
        assertThat(results).hasSize(3);
    }

    @Test
    @DisplayName("Should return latest by playerId and type")
    void should_return_latest_by_playerId_and_type() {
        List<TrainingEvent> events = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            events.add(
                TrainingEvent.builder()
                    .type(TrainingEvent.TrainingType.DECLINE_TRAINING)
                    .modifier(null)
                    .occurredAt(InstantProvider.now().plus(i+1, ChronoUnit.HOURS))
                    .player(Player.builder()
                        .id(Player.PlayerId.of("id"))
                        .name("name")
                        .preferredPosition(PlayerPosition.GOALKEEPER)
                        .build())
                    .teamId(Team.TeamId.of("teamId"))
                    .points(i+1)
                    .skill(PlayerSkill.REFLEXES)
                    .pointsAfterTraining(21)
                    .pointsBeforeTraining(19)
                    .build()
            );
        }
        trainingEventMongoRepository.saveAll(events);
        Optional<TrainingEvent> event = trainingEventReadRepository.findLatestByPlayerIdAndType(Player.PlayerId.of("id"),
            TrainingEvent.TrainingType.DECLINE_TRAINING);
        assertThat(event).isPresent();
        assertThat(event.get().getPoints()).isEqualTo(3);

    }

    @Test
    @DisplayName("Should return first by reference")
    void should_return_first_by_reference() {
        List<TrainingEvent> events = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            events.add(
                TrainingEvent.builder()
                    .reference("123")
                    .type(TrainingEvent.TrainingType.DECLINE_TRAINING)
                    .modifier(null)
                    .occurredAt(InstantProvider.now().plus(i+1, ChronoUnit.HOURS))
                    .player(Player.builder()
                        .id(Player.PlayerId.of("id"))
                        .name("name")
                        .preferredPosition(PlayerPosition.GOALKEEPER)
                        .build())
                    .teamId(Team.TeamId.of("teamId"))
                    .points(i+1)
                    .skill(PlayerSkill.REFLEXES)
                    .pointsAfterTraining(21)
                    .pointsBeforeTraining(19)
                    .build()
            );
        }
        trainingEventMongoRepository.saveAll(events);
        Optional<TrainingEvent> event = trainingEventReadRepository.findFirstByReferenceOrderByOccurredAtDesc("123");
        assertThat(event).isPresent();
        assertThat(event.get().getPoints()).isEqualTo(3);
    }
}