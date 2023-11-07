package com.kjeldsen.player.application.usecases;

import com.kjeldsen.events.domain.EventId;
import com.kjeldsen.player.application.publisher.PlayerPublisher;
import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.events.PlayerCreationEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.player.PlayerCreationEventWriteRepository;
import com.kjeldsen.player.domain.repositories.player.PlayerPositionTendencyReadRepository;
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

    private final PlayerPublisher playerPublisher;
    private final PlayerPositionTendencyReadRepository playerPositionTendencyReadRepository;
    private final PlayerCreationEventWriteRepository playerCreationEventWriteRepository;

    public void create(NewPlayer newPlayer) {
        log.info("Creating player {}", newPlayer);
        PlayerPositionTendency positionTendencies = playerPositionTendencyReadRepository.get(newPlayer.getPosition());

        PlayerCreationEvent playerCreationEvent = PlayerCreationEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(Player.PlayerId.generate())
            .name(PlayerProvider.name())
            .age(newPlayer.getAge())
            .position(newPlayer.getPosition())
            .initialSkills(PlayerProvider.skillsBasedOnTendency(positionTendencies, newPlayer.getPoints()))
            .teamId(newPlayer.getTeamId())
            .playerCategory(newPlayer.getPlayerCategory())
            .build();

        playerCreationEventWriteRepository.save(playerCreationEvent);

        playerPublisher.saveAndPublish(Player.creation(playerCreationEvent));
    }

    @Builder
    @Getter
    @Setter
    public static class NewPlayer {
        private Integer age;
        private PlayerPosition position;
        private int points;
        private Team.TeamId teamId;
        private PlayerCategory playerCategory;
    }

}
