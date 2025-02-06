package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.player.application.usecases.GetHistoricalTrainingUseCase;
import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.application.usecases.trainings.GetActiveScheduledTrainingsUseCase;
import com.kjeldsen.player.application.usecases.trainings.GetHistoricalTeamPlayerTrainingUseCase;
import com.kjeldsen.player.application.usecases.trainings.SchedulePlayerTrainingUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventReadRepository;
import com.kjeldsen.player.rest.api.TrainingApiDelegate;
import com.kjeldsen.player.rest.mapper.PlayerMapper;
import com.kjeldsen.player.rest.mapper.PlayerTrainingResponseMapper;
import com.kjeldsen.player.rest.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Component
public class TrainingDelegate implements TrainingApiDelegate {

    // Common
    private final GetTeamUseCase getTeamUseCase;
    private final PlayerReadRepository playerReadRepository;


    private final SchedulePlayerTrainingUseCase schedulePlayerTrainingUseCase;
    private final GetHistoricalTeamPlayerTrainingUseCase getHistoricalTeamPlayerTrainingUseCase;
    private final GetActiveScheduledTrainingsUseCase getActiveScheduledTrainingsUseCase;

    @Override
    public ResponseEntity<Void> schedulePlayerTraining(String playerId, SchedulePlayerTrainingRequest schedulePlayerTrainingRequest) {
        // Access denied as the player is not in your Team
        Optional<Player> optionalPlayer = playerReadRepository.findOneById(Player.PlayerId.of(playerId));
        if (optionalPlayer.isPresent()) {
            if (!optionalPlayer.get().getTeamId().equals(getTeamUseCase.get(SecurityUtils.getCurrentUserId()).getId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }


        com.kjeldsen.player.domain.PlayerSkill skill = PlayerMapper.INSTANCE.map(schedulePlayerTrainingRequest.getSkill().getValue());
        schedulePlayerTrainingUseCase.schedule(Player.PlayerId.of(playerId), skill);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<PlayerScheduledTrainingResponse>> getScheduledPlayerTrainings(String teamId) {
        // Access denied as the path teamId is different that the teamId from Token
        if (!Objects.equals(getTeamUseCase.get(SecurityUtils.getCurrentUserId()).getId().value(), teamId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<PlayerScheduledTrainingResponse> response = new ArrayList<>();
        List<GetActiveScheduledTrainingsUseCase.PlayerScheduledTraining> playerScheduled =
            getActiveScheduledTrainingsUseCase.get(Team.TeamId.of(teamId));
        playerScheduled.forEach(e -> {
            PlayerScheduledTrainingResponse scheduledTrainingResponse = new PlayerScheduledTrainingResponse()
                .player(PlayerMapper.INSTANCE.playerResponseMap(e.getPlayer()))
                .skills(e.getSkills().stream()
                    .map(skillDomain -> PlayerMapper.INSTANCE.playerSkillMap(skillDomain.name()))
                    .toList());

            response.add(scheduledTrainingResponse);
        });
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TeamHistoricalTrainingResponse> getTeamHistoricalPlayerTrainings(String teamId) {
        // Access denied as the path teamId is different that the teamId from Token
        if (!Objects.equals(getTeamUseCase.get(SecurityUtils.getCurrentUserId()).getId().value(), teamId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Map<String, List<GetHistoricalTeamPlayerTrainingUseCase.PlayerTraining>> events =
            getHistoricalTeamPlayerTrainingUseCase.get(teamId);

        Map<String, List<PlayerTrainingResponse>> responseTrainings = PlayerTrainingResponseMapper
            .INSTANCE.fromPlayerTrainingEventsMap(events);

        TeamHistoricalTrainingResponse response = new TeamHistoricalTrainingResponse().trainings(responseTrainings);

        return ResponseEntity.ok(response);
    }

    private PlayerSkill playerSkill2DomainPlayerSkill(com.kjeldsen.player.domain.PlayerSkill playerSkill) {
        return PlayerSkill.valueOf(playerSkill.name()); // TODO MUDO CAMBIA ESTO A UN MAPPER
    }

    private PlayerTrainingResponse playerTrainingEvent2PlayerTrainingResponse(PlayerTrainingEvent playerTrainingEvent) {
        return new PlayerTrainingResponse()
            .currentDay(playerTrainingEvent.getCurrentDay())
            .playerId(playerTrainingEvent.getPlayerId().toString())
            .skill(playerSkill2DomainPlayerSkill(playerTrainingEvent.getSkill()))
            .actualPoints(playerTrainingEvent.getActualPoints())
            .pointsBeforeTraining(playerTrainingEvent.getPointsBeforeTraining())
            .pointsAfterTraining(playerTrainingEvent.getPointsAfterTraining());
    }

}
