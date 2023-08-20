package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerPositionTendencyReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class GeneratePlayersUseCase {

    private final PlayerPositionTendencyReadRepository playerPositionTendencyReadRepository;
    private final CreatePlayerUseCase createPlayerUseCase;

    public List<Player> generate(int numberOfPlayers, Team.TeamId teamId) {
        log.info("Generating {} players", numberOfPlayers);
        return IntStream.range(0, numberOfPlayers)
            .mapToObj(i -> playerPositionTendencyReadRepository.get(PlayerProvider.position()))
            .map(positionTendencies -> createPlayerUseCase.create(CreatePlayerUseCase.NewPlayer.builder()
                     .age(PlayerProvider.age())
                     .points(200)
                     .position(positionTendencies.getPosition())
                     .teamId(teamId)
                 .build())).toList();
    }
}
