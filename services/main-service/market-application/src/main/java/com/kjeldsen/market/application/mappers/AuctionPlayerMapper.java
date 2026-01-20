package com.kjeldsen.market.application.mappers;

import com.kjeldsen.market.domain.AuctionPlayer;
import com.kjeldsen.market.rest.model.AuctionPlayerResponseActualSkillsValue;
import com.kjeldsen.market.rest.model.AuctionPlayerSkills;
import com.kjeldsen.player.rest.model.PlayerResponse;
import com.kjeldsen.player.rest.model.PlayerResponseActualSkillsValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Map;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface AuctionPlayerMapper {

    AuctionPlayerMapper INSTANCE = Mappers.getMapper(AuctionPlayerMapper.class);

    @Mapping(source = "actualSkills", target = "actualSkills", qualifiedByName = "mapSkills")
    AuctionPlayer map(PlayerResponse playerResponse);

    @Named("mapSkills")
    default Map<String, AuctionPlayer.AuctionPlayerSkill> mapSkills(
        Map<String, PlayerResponseActualSkillsValue> inputSkills
    ) {
        if (inputSkills == null) return null;

        return inputSkills.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> {
                    PlayerResponseActualSkillsValue value = entry.getValue();
                    AuctionPlayer.AuctionPlayerSkill skill = new AuctionPlayer.AuctionPlayerSkill();
                    if (value != null && value.getPlayerSkills() != null) {
                        skill.setActual(value.getPlayerSkills().getActual());
                        skill.setPotential(value.getPlayerSkills().getPotential());
                    }
                    return skill;
                }
            ));
    }

}
