package com.kjeldsen.match.rest.mapper;

import com.kjeldsen.match.rest.model.TeamFormationValidationResponse;
import com.kjeldsen.match.validation.TeamFormationValidationResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeamFormationValidationMapper {
    TeamFormationValidationMapper INSTANCE = Mappers.getMapper(TeamFormationValidationMapper.class);

    TeamFormationValidationResponse map(TeamFormationValidationResult result);
}