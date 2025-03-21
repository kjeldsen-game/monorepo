package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.application.usecases.trainings.GetHistoricalTeamPlayerTrainingUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.rest.model.PlayerResponse;
import com.kjeldsen.player.rest.model.PlayerSkill;
import com.kjeldsen.player.rest.model.PlayerTrainingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(uses = {IdMapper.class})
public interface PlayerTrainingResponseMapper {

    PlayerTrainingResponseMapper INSTANCE = Mappers.getMapper(PlayerTrainingResponseMapper.class);

    default PlayerSkill fromPlayerSkillDomain(com.kjeldsen.player.domain.PlayerSkill playerSkillDomain) {
        return PlayerSkill.valueOf(playerSkillDomain.name());
    }

    PlayerTrainingResponse fromPlayerTrainingEvent(PlayerTrainingEvent playerTrainingEvent);

    default PlayerTrainingResponse fromPlayerTraining(GetHistoricalTeamPlayerTrainingUseCase.PlayerTraining playerTraining) {
        Player player = playerTraining.getPlayer();
        PlayerTrainingResponse response = fromPlayerTrainingEvent(playerTraining.getPlayerTrainingEvent());

        PlayerResponse playerResponse = PlayerMapper.INSTANCE.playerResponseMap(player);
        response.player(playerResponse);
        return response;
    }

    default Map<String, List<PlayerTrainingResponse>> fromPlayerTrainingEventsMap(
        Map<String, List<GetHistoricalTeamPlayerTrainingUseCase.PlayerTraining>> eventsMap) {

        return eventsMap.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream()
                    .map(this::fromPlayerTraining)
                    .toList()));
    }
}
