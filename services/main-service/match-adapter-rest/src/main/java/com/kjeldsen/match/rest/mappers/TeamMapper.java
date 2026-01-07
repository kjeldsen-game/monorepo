package com.kjeldsen.match.rest.mappers;

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
        if (team == null || team.getModifiers() == null ) {
            return null;
        }
        Modifiers modifiers = new Modifiers();
        modifiers.setTactic(Tactic.valueOf(team.getModifiers().getTactic().name()));
        modifiers.setVerticalPressure(VerticalPressure.valueOf(team.getModifiers().getVerticalPressure().name()));
        modifiers.setHorizontalPressure(HorizontalPressure.valueOf(team.getModifiers().getHorizontalPressure().name()));

        return modifiers;
    }



    com.kjeldsen.match.domain.modifers.TeamModifiers map(TeamModifiers teamModifiers);

    TeamModifiers map(com.kjeldsen.match.domain.modifers.TeamModifiers teamModifiers);
}
