package com.kjeldsen.match.rest.mapper;

import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.rest.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {IdMapper.class})
public interface TeamMapper {
    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    @Mapping(target = "modifiers", expression = "java(mapToModifiers(team))")
    TeamResponse map(Team team);

    default Modifiers mapToModifiers(Team team) {
        if (team == null) return null;
        Modifiers modifiers = new Modifiers();
        modifiers.setTactic(Tactic.valueOf(team.getTactic().name()));
        modifiers.setVerticalPressure(VerticalPressure.valueOf(team.getVerticalPressure().name()));
        modifiers.setHorizontalPressure(HorizontalPressure.valueOf(team.getHorizontalPressure().name()));
        return modifiers;
    }

    com.kjeldsen.match.domain.modifers.TeamModifiers map(TeamModifiers teamModifiers);

    TeamModifiers map(com.kjeldsen.match.domain.modifers.TeamModifiers teamModifiers);
}
