package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.generator.BloomPhaseGenerator;
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
        log.info("CreatePlayerUseCase for player {}", newPlayer);
        PlayerPositionTendency positionTendencies = playerPositionTendencyReadRepository.get(newPlayer.getPosition());

        Player player = Player.builder()
            .id(Player.PlayerId.generate())
            .name(PlayerProvider.name())
            .age(PlayerAge.generateAgeOfAPlayer())
            .preferredPosition(newPlayer.getPosition())
            .position(null)
            .playerOrder(PlayerOrder.NONE)
            .status(PlayerStatus.INACTIVE)
            .actualSkills(PlayerProvider.skillsBasedOnTendency(positionTendencies, newPlayer.getPoints()))
            .teamId(newPlayer.getTeamId())
            .bloomYear(BloomPhaseGenerator.generateBloomPhaseYear())
            .category(newPlayer.getPlayerCategory())
            .economy(Player.Economy.builder().build())
            .build();
        player.negotiateSalary();
        playerWriteRepository.save(player);
    }

    @Builder
    @Getter
    @Setter
    public static class NewPlayer {
        private PlayerPosition position;
        private int points;
        private Team.TeamId teamId;
        private PlayerCategory playerCategory;
    }
}
