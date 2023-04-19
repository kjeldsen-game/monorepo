package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerPositionTendencyReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreatePlayerUseCase {

    private final PlayerWriteRepository playerWriteRepository;
    private final PlayerPositionTendencyReadRepository playerPositionTendencyReadRepository;

    public void create(NewPlayer newPlayer) {
        log.info("Creating player {}", newPlayer);
        PlayerPositionTendency positionTendencies = playerPositionTendencyReadRepository.get(newPlayer.getPosition());
        Player player = Player.builder()
            .id(Player.PlayerId.generate())
            .name(PlayerProvider.name())
            .age(newPlayer.getAge())
            .position(newPlayer.getPosition())
            .actualSkills(PlayerProvider.skillsBasedOnTendency(positionTendencies, newPlayer.getPoints()))
            .teamId(newPlayer.getTeamId())
            .build();

        log.info("Generated player {}", player);
        playerWriteRepository.save(player);
    }

    @Builder
    @Getter
    @Setter
    public static class NewPlayer {
        private Integer age;
        private PlayerPosition position;
        private int points;
        private Team.TeamId teamId;
    }

}
