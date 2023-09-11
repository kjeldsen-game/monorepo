package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.UpdatePlayerPositionTendencyUseCase;
import com.kjeldsen.player.application.usecases.UpdatePlayerTendencies;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;


import com.kjeldsen.player.domain.repositories.PlayerPositionTendencyReadRepository;
import com.kjeldsen.player.rest.api.PlayerPositionTendenciesApiDelegate;
import com.kjeldsen.player.rest.mapper.PlayerMapper;
import com.kjeldsen.player.rest.mapper.PlayerPositionTendencyMapper;
import com.kjeldsen.player.rest.model.PlayerPositionTendencyResponse;
import com.kjeldsen.player.rest.model.PlayerSkill;
import com.kjeldsen.player.rest.model.PlayerSkills;
import com.kjeldsen.player.rest.model.UpdatePlayerPositionTendencyRequestValue;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class PlayerPositionTendenciesDelegate implements PlayerPositionTendenciesApiDelegate {

    private final UpdatePlayerPositionTendencyUseCase updatePlayerPositionTendencyUseCase;
    private final PlayerPositionTendencyReadRepository playerPositionTendencyReadRepository;

    @Override
    public ResponseEntity<List<PlayerPositionTendencyResponse>> getAllPlayerPositionTendencies() {
        List<PlayerPositionTendencyResponse> response = playerPositionTendencyReadRepository.find()
            .stream()
            .map(PlayerPositionTendencyMapper.INSTANCE::map)
            .toList();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PlayerPositionTendencyResponse> getPlayerPositionTendency(com.kjeldsen.player.rest.model.PlayerPosition position) {
        PlayerPositionTendency playerPositionTendency = playerPositionTendencyReadRepository.get(PlayerPosition.valueOf(position.name()));
        PlayerPositionTendencyResponse response = PlayerPositionTendencyMapper.INSTANCE.map(playerPositionTendency);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PlayerPositionTendencyResponse> updatePlayerPositionTendency(com.kjeldsen.player.rest.model.PlayerPosition position,
                                                                                       Map<String, UpdatePlayerPositionTendencyRequestValue> tendencies) {

        PlayerPositionTendency updatedPlayerPositionTendency = updatePlayerPositionTendencyUseCase.update(
            UpdatePlayerTendencies.builder()
                .position(PlayerMapper.INSTANCE.playerPositionMap(position))
                .tendencies(PlayerPositionTendencyMapper.INSTANCE.map(tendencies))
                .build());

        PlayerPositionTendencyResponse response = PlayerPositionTendencyMapper.INSTANCE.map(updatedPlayerPositionTendency);
        return ResponseEntity.ok(response);
    }
}
