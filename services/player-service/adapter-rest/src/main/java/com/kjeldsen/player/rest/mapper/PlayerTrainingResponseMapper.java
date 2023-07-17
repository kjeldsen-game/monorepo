package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.rest.model.PlayerSkill;
import com.kjeldsen.player.rest.model.PlayerTrainingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {IdMapper.class})
public interface PlayerTrainingResponseMapper {

    PlayerTrainingResponseMapper INSTANCE = Mappers.getMapper(PlayerTrainingResponseMapper.class);

    PlayerTrainingResponse fromPlayerTrainingEvent(PlayerTrainingEvent playerTrainingEvent);

    default PlayerSkill fromPlayerSkillDomain(com.kjeldsen.player.domain.PlayerSkill playerSkillDomain) {
        return PlayerSkill.valueOf(playerSkillDomain.name());
    }


}
