package com.kjeldsen.player.application.publisher;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.repositories.player.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PlayerPublisher {

    private final PlayerWriteRepository playerWriteRepository;

    public void saveAndPublish(Player player) {
        playerWriteRepository.save(player);
        // TODO publish to default player topic
        log.info("Saved and published player {}", player);
    }

}
