package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.Player;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class FindPlayersUseCase {

    private final PlayerReadRepository playerReadRepository;

    public List<Player> find(FindPlayersQuery query) {
        return playerReadRepository.find(query);
    }
}
