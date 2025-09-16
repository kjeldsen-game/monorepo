package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.models.training.TrainingEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.training.TrainingEventReadRepository;
import com.kjeldsen.player.domain.repositories.training.TrainingEventWriteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class BaseTrainingUseCase {

    @Autowired
    protected TrainingEventWriteRepository trainingEventWriteRepository;
    @Autowired
    protected TrainingEventReadRepository trainingEventReadRepository;

    protected TrainingEvent buildTrainingEvent(Player player, PlayerSkill skill,
      Integer points, TrainingEvent.TrainingType type, boolean isModifierActive) {

        return TrainingEvent.builder()
            .type(type)
            .modifier(getTrainingModifier(type, isModifierActive))
            .occurredAt(InstantProvider.now())
            .player(Player.builder()
                .id(player.getId())
                .name(player.getName())
                .preferredPosition(player.getPreferredPosition())
                .build())
            .teamId(player.getTeamId())
            .points(points)
            .skill(skill)
            .build();
    }
    protected TrainingEvent.TrainingModifier getTrainingModifier(TrainingEvent.TrainingType type, boolean isModifierActive) {
        return switch (type) {
            case DECLINE_TRAINING -> isModifierActive ? TrainingEvent.TrainingModifier.FALL_OFF_CLIFF : null;
            case PLAYER_TRAINING -> isModifierActive ? TrainingEvent.TrainingModifier.BLOOM : null;
            default -> null;
        };
    }
}
