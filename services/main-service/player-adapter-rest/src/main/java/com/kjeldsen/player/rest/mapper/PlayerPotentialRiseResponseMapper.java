package com.kjeldsen.player.rest.mapper;


import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import com.kjeldsen.player.rest.model.PlayerPotentialRiseResponse;
import com.kjeldsen.player.rest.model.PlayerSkill;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {IdMapper.class})
public interface PlayerPotentialRiseResponseMapper {

    PlayerPotentialRiseResponseMapper INSTANCE = Mappers.getMapper(PlayerPotentialRiseResponseMapper.class);

    default PlayerSkill fromPlayerSkillDomain(com.kjeldsen.player.domain.PlayerSkill playerSkillDomain) {
        return PlayerSkill.valueOf(playerSkillDomain.name());
    }

}