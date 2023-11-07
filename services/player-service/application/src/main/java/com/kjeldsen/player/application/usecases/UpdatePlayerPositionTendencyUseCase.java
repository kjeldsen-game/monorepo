package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.repositories.player.PlayerPositionTendencyWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
}
