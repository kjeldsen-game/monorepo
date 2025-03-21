package com.kjeldsen.player.application.usecases;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.events.PlayerCreationEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
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

        PlayerCreationEvent playerCreationEvent = PlayerCreationEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(Player.PlayerId.generate())
            .name(PlayerProvider.name())
            .age(PlayerAge.generateAgeOfAPlayer())
            .position(newPlayer.getPosition())
            .initialSkills(PlayerProvider.skillsBasedOnTendency(positionTendencies, newPlayer.getPoints()))
            .teamId(newPlayer.getTeamId())
            .playerCategory(newPlayer.getPlayerCategory())
            .build();

        playerWriteRepository.save(Player.creation(playerCreationEvent));
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
