package com.kjeldsen.player.rest.mapper.team;

import com.kjeldsen.player.application.usecases.player.ValidateTeamLineupUseCase;
import com.kjeldsen.player.rest.model.TeamFormationValidationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeamFormationValidationMapper {
    TeamFormationValidationMapper INSTANCE = Mappers.getMapper(TeamFormationValidationMapper.class);

    TeamFormationValidationResponse map(ValidateTeamLineupUseCase.TeamFormationValidationResult result);
}
