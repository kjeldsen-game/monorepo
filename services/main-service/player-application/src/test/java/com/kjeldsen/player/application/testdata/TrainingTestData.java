package com.kjeldsen.player.application.testdata;

import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.models.training.TrainingEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;

import java.util.*;

public class TrainingTestData {

    public static Player getTestPlayerForTrainingEvents() {
        return Player.builder().id(Player.PlayerId.of("playerId")).name("player")
            .teamId(Team.TeamId.of("teamId"))
            .age(PlayerAge.generateAgeOfAPlayer())
            .preferredPosition(PlayerPosition.GOALKEEPER)
            .build();
    }

    public static List<TrainingEvent> getTrainingEvents(TrainingEvent.TrainingModifier modifier, TrainingEvent.TrainingType type) {
        TrainingEvent trainingEvent = TrainingEvent.builder()
            .type(type)
            .modifier(modifier)
            .occurredAt(InstantProvider.now())
            .player(Player.builder()
                .id(getTestPlayerForTrainingEvents().getId())
                .name(getTestPlayerForTrainingEvents().getName())
                .preferredPosition(getTestPlayerForTrainingEvents().getPreferredPosition())
                .build())
            .teamId(getTestPlayerForTrainingEvents().getTeamId())
            .points(2)
            .skill(PlayerSkill.REFLEXES)
            .build();

    return List.of(trainingEvent);
    }
}
