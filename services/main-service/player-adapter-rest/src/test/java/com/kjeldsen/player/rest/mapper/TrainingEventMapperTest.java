package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.models.training.TrainingEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.rest.mapper.training.TrainingEventMapper;
import com.kjeldsen.player.rest.model.PlayerResponse;
import com.kjeldsen.player.rest.model.TrainingEventResponse;
import com.kjeldsen.player.rest.model.TrainingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TrainingEventMapperTest {

    public static Player getTestPlayerForTrainingEvents() {
        return Player.builder()
            .id(Player.PlayerId.of("playerId"))
            .name("Test Player")
            .teamId(Team.TeamId.of("teamId"))
            .preferredPosition(PlayerPosition.GOALKEEPER)
            .build();
    }

    @Test
    @DisplayName("Should map training event to training event response")
    void should_map_training_event_to_training_event_response() {
        TrainingEvent trainingEvent = TrainingEvent.builder()
            .type(TrainingEvent.TrainingType.POTENTIAL_RISE)
            .reference("reference")
            .modifier(null)
            .occurredAt(InstantProvider.now())
            .player(getTestPlayerForTrainingEvents())
            .teamId(getTestPlayerForTrainingEvents().getTeamId())
            .points(2)
            .currentDay(12)
            .pointsBeforeTraining(10)
            .pointsAfterTraining(12)
            .skill(PlayerSkill.REFLEXES)
            .build();

        TrainingEventResponse response = TrainingEventMapper.INSTANCE.fromTrainingEvent(trainingEvent);

        TrainingEventResponse expected = new TrainingEventResponse()
            .trainingType(TrainingType.POTENTIAL_RISE)
            .modifier(null)
            .occurredAt(String.valueOf(trainingEvent.getOccurredAt()))
            .player(
                new PlayerResponse().id("playerId").name("Test Player")
                    .teamId("teamId").preferredPosition(com.kjeldsen.player.rest.model.PlayerPosition.GOALKEEPER)
            )
            .points(2)
            .currentDay(12)
            .pointsBeforeTraining(10)
            .pointsAfterTraining(12)
            .skill(com.kjeldsen.player.rest.model.PlayerSkill.REFLEXES);

        assertThat(response)
            .usingRecursiveComparison()
            .comparingOnlyFields(
                "trainingType",
                "modifier",
                "occurredAt",
                "player.id",
                "player.name",
                "player.teamId",
                "player.preferredPosition",
                "points",
                "currentDay",
                "pointsBeforeTraining",
                "pointsAfterTraining",
                "skill"
            )
            .isEqualTo(expected);
    }
}