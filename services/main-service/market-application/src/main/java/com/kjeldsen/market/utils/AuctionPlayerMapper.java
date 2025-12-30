package com.kjeldsen.market.utils;

import com.kjeldsen.lib.model.player.PlayerAgeClient;
import com.kjeldsen.lib.model.player.PlayerClient;
import com.kjeldsen.lib.model.player.PlayerSkillsClient;
import com.kjeldsen.market.domain.AuctionPlayer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface AuctionPlayerMapper {

    AuctionPlayerMapper INSTANCE = Mappers.getMapper(AuctionPlayerMapper.class);

    @Mapping(source = "actualSkills", target = "actualSkills", qualifiedByName = "mapSkills")
    AuctionPlayer map(PlayerClient playerClient);

    default AuctionPlayer.AuctionPlayerAge mapAge(PlayerAgeClient playerAge) {
        if (playerAge == null) {
            return null;
        }
        AuctionPlayer.AuctionPlayerAge age = new AuctionPlayer.AuctionPlayerAge();
        age.setYears(playerAge.getYears());
        age.setMonths(playerAge.getMonths());
        age.setDays(playerAge.getDays());
        return age;
    }

    @Named("mapSkills")
    default Map<String, AuctionPlayer.AuctionPlayerSkill> mapSkills(Map<String, PlayerSkillsClient> skills) {
        if (skills == null) {
            return null;
        }
        return skills.entrySet().stream().collect(
                java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        e -> {
                            AuctionPlayer.AuctionPlayerSkill skill = new AuctionPlayer.AuctionPlayerSkill();
                            skill.setActual(e.getValue().getActual());
                            skill.setPotential(e.getValue().getPotential());
                            return skill;
                        }
                )
        );
    }
}
