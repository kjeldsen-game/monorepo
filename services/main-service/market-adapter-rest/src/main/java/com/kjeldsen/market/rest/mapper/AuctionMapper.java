package com.kjeldsen.market.rest.mapper;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.AuctionPlayer;
import com.kjeldsen.market.rest.model.*;
import jdk.jfr.Name;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(uses = {IdMapper.class})
public interface AuctionMapper {

    AuctionMapper INSTANCE = Mappers.getMapper(AuctionMapper.class);

    @Mapping(source = "bids", target = "bidders", qualifiedByName = "bidsCount")
    AuctionResponse map(Auction auction);

    AuctionPlayerResponse map(AuctionPlayer player);

    @Named("bidsCount")
    default Integer map(List<Auction.Bid> bids) {
        if (bids == null) {
            return 0;
        }
        return bids.size();
    }

    default Bid map(Auction.Bid bid) {
        if (bid == null) {
            return null;
        }
        Bid responseBid = new Bid();
        responseBid.setTeam(bid.getTeamId());
        responseBid.setAmount(bid.getAmount());
        responseBid.setTimestamp(String.valueOf(bid.getTimestamp()));
        return responseBid;
    }

    default Map<String, AuctionPlayerResponseActualSkillsValue> map(
        Map<String, AuctionPlayer.AuctionPlayerSkill> skills
    ) {
        if (skills == null) {
            return null;
        }
        return skills.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> {
                    AuctionPlayer.AuctionPlayerSkill skill = entry.getValue();
                    AuctionPlayerResponseActualSkillsValue responseSkill = new AuctionPlayerResponseActualSkillsValue();
                    AuctionPlayerSkills innerSkill = new AuctionPlayerSkills();
                    innerSkill.setActual(skill.getActual());
                    innerSkill.setPotential(skill.getPotential());
                    responseSkill.setPlayerSkills(innerSkill);
                    return responseSkill;
                }
            ));
    }
}

