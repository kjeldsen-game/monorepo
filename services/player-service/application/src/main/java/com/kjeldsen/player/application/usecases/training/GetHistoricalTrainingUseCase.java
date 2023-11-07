package com.kjeldsen.player.application.usecases.training;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.repositories.player.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.training.PlayerTrainingEventReadRepository;
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

    public List<PlayerTrainingEvent> get(Player.PlayerId playerId) {

        playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException(String.format("Player not found with ID %s", playerId)));

        return playerTrainingEventReadRepository.findAllByPlayerId(playerId);
    }

}
