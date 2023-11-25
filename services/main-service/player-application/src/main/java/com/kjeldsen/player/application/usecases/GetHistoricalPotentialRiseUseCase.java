package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.repositories.PlayerPotentialRiseScheduledEventReadRepository;
import com.kjeldsen.player.domain.repositories.player.PlayerPotentialRiseEventReadRepository;
import com.kjeldsen.player.domain.repositories.player.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.training.PlayerTrainingEventReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class GetHistoricalPotentialRiseUseCase {

    private final PlayerPotentialRiseEventReadRepository playerPotentialRiseEventReadRepository;
    private final PlayerReadRepository playerReadRepository;

    public List<PlayerPotentialRiseEvent> get(Player.PlayerId playerId) {

        playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException(String.format("Player not found with ID %s", playerId)));

        return playerPotentialRiseEventReadRepository.findAllByPlayerId(playerId);
    }

}
