package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.domain.repositories.PlayerPositionTendencyWriteRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class UpdatePlayerPositionTendencyUseCase {

    private final PlayerPositionTendencyWriteRepository playerPositionTendencyWriteRepository;

    public PlayerPositionTendency update(UpdatePlayerTendencies updatePlayerTendencies) {
        PlayerPositionTendency playerPositionTendency = PlayerPositionTendency.builder()
            .position(updatePlayerTendencies.getPosition())
            .tendencies(updatePlayerTendencies.getTendencies())
            .build();
        return playerPositionTendencyWriteRepository.save(playerPositionTendency);
    }

    @Builder
    @Getter
    public static class UpdatePlayerTendencies {
        private PlayerPosition position;
        private Map<PlayerSkill, PlayerSkills> tendencies;
    }
}
