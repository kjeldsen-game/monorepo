package com.kjeldsen.player.rest.mapper.player;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.rest.mapper.common.IdMapper;
import com.kjeldsen.player.rest.mapper.common.EnumMapper;
import com.kjeldsen.player.rest.model.PlayerResponse;
import com.kjeldsen.player.rest.model.PlayerSkill;
import com.kjeldsen.player.rest.model.PlayerSkillRelevance;
import com.kjeldsen.player.rest.model.PlayerStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Objects;

@Mapper(uses = {IdMapper.class})
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    PlayerResponse playerResponseMap(Player player);

    default PlayerPosition map(com.kjeldsen.player.rest.model.PlayerPosition position) {
        return EnumMapper.mapEnum(position, PlayerPosition.class);
    }

    default com.kjeldsen.player.rest.model.PlayerPosition map(PlayerPosition position) {
        return EnumMapper.mapEnum(position, com.kjeldsen.player.rest.model.PlayerPosition.class);
    }

    default com.kjeldsen.player.domain.PlayerStatus map(PlayerStatus status) {
        return EnumMapper.mapEnum(status, com.kjeldsen.player.domain.PlayerStatus.class);
    }

    default com.kjeldsen.player.domain.PlayerSkill map(String playerSkill) {
        return com.kjeldsen.player.domain.PlayerSkill.valueOf(playerSkill);
    }

    default com.kjeldsen.player.rest.model.PlayerSkills map(com.kjeldsen.player.domain.PlayerSkills playerSkills) {
        com.kjeldsen.player.rest.model.PlayerSkills var1 = new com.kjeldsen.player.rest.model.PlayerSkills();
        var1.setActual(playerSkills.getActual());
        var1.setPotential(playerSkills.getPotential());
        if (Objects.nonNull(playerSkills.getPlayerSkillRelevance())) {
            var1.setPlayerSkillRelevance(PlayerSkillRelevance.valueOf(playerSkills.getPlayerSkillRelevance().name()));
        }
        return var1;
    }

    default com.kjeldsen.player.rest.model.PlayerAge map(com.kjeldsen.player.domain.PlayerAge playerAge) {
        com.kjeldsen.player.rest.model.PlayerAge var1 = new com.kjeldsen.player.rest.model.PlayerAge();
        var1.setYears(playerAge.getYears());
        var1.setMonths(BigDecimal.valueOf(playerAge.getMonths()));
        var1.setDays(BigDecimal.valueOf(playerAge.getDays()));
        return var1;
    }
}
