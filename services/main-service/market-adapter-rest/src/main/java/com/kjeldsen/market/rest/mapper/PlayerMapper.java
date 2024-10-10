package com.kjeldsen.market.rest.mapper;

import com.kjeldsen.market.rest.model.*;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper(uses = {IdMapper.class})
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    PlayerResponse playerResponseMap(Player player);

    default PlayerPosition playerPositionMap(com.kjeldsen.market.rest.model.PlayerPosition position) {
        return PlayerPosition.valueOf(position.name());
    }

    default PlayerSkill playerSkillMap(String playerSkill) {
        return PlayerSkill.valueOf(playerSkill);
    }

    default com.kjeldsen.player.domain.PlayerSkill map(String playerSkill) {
        return com.kjeldsen.player.domain.PlayerSkill.valueOf(playerSkill);
    }

    default com.kjeldsen.market.rest.model.PlayerSkills map(com.kjeldsen.player.domain.PlayerSkills playerSkills) {
        com.kjeldsen.market.rest.model.PlayerSkills var1 = new com.kjeldsen.market.rest.model.PlayerSkills();
        var1.setActual(playerSkills.getActual());
        var1.setPotential(playerSkills.getPotential());
        var1.setPlayerSkillRelevance(PlayerSkillRelevance.valueOf(playerSkills.getPlayerSkillRelevance().name()));
        return var1;
    }

    default com.kjeldsen.market.rest.model.PlayerAge map(com.kjeldsen.player.domain.PlayerAge playerAge) {
        com.kjeldsen.market.rest.model.PlayerAge var1 = new com.kjeldsen.market.rest.model.PlayerAge();
        var1.setYears(BigDecimal.valueOf(playerAge.getYears()));
        var1.setMonths(BigDecimal.valueOf(playerAge.getMonths()));
        var1.setDays(BigDecimal.valueOf(playerAge.getDays()));
        return var1;
    }
}
