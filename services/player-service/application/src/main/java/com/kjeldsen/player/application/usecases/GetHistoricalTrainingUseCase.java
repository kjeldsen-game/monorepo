package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class GetHistoricalTrainingUseCase {

    private final PlayerTrainingEventReadRepository playerTrainingEventReadRepository;
    private final PlayerReadRepository playerReadRepository;

    public List<PlayerTrainingEvent> get(PlayerId playerId) {

        playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));

        return playerTrainingEventReadRepository.findAllByPlayerId(playerId);
    }

}
