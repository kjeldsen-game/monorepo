package com.kjeldsen.market.application;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.repositories.queries.FindAuctionsQuery;
import com.kjeldsen.player.domain.PlayerPosition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetAuctionUseCase {

    private final AuctionReadRepository auctionReadRepository;

    public List<Auction> getAuctions(Double maxBid, Double minBid, Integer maxAge, Integer minAge,
                                            PlayerPosition position, String skills, String potentialSkills, String playerId) {

        log.info("GetMarketAuctionsUseCase for maxBid = {}, minBid = {}, maxAge = {} minAge = {}" +
                " position = {} skills = {} potentialSkills = {} playerId = {}", maxBid, minBid, maxAge, minAge,
            position, skills, potentialSkills, playerId);

        List<Auction> auctions =  auctionReadRepository.findAllByQuery(
            FindAuctionsQuery.builder()
                .maxAge(maxAge)
                .minAge(minAge)
                .playerId(playerId)
                .skills(parsePlayerSkills(skills, potentialSkills))
                .auctionStatus(List.of(Auction.AuctionStatus.ACTIVE))
                .maxAverageBid(maxBid)
                .minAverageBid(minBid)
                .build());

        log.info("Auctions found: {}", auctions.size());
        return auctions;
    }

    private List<FindAuctionsQuery.PlayerSkillFilter> parsePlayerSkills(
        String actualSkillsString, String potentialSkillsString) {
        List<FindAuctionsQuery.PlayerSkillFilter> playerSkills = new ArrayList<>();
        parseSkillString(playerSkills, actualSkillsString, true);
        parseSkillString(playerSkills, potentialSkillsString, false);
        return playerSkills;
    }

    private void parseSkillString(
        List<FindAuctionsQuery.PlayerSkillFilter> filteredSkills, String skillString,
        boolean isActualSkills) {
        if (skillString != null && !skillString.isEmpty()) {
            String[] skills = skillString.split(",");
            for (String skill : skills) {
                String[] skillParts = skill.split(":");
                String domainPlayerSkill;
                int minValue;
                int maxValue;

                try {
                    domainPlayerSkill = skillParts[0];

                    minValue = (skillParts.length > 1 && isParsable(skillParts[1]))
                        ? Integer.parseInt(skillParts[1])
                        : 0;

                    maxValue = (skillParts.length == 3 && isParsable(skillParts[2]))
                        ? Integer.parseInt(skillParts[2])
                        : 100;

                    int index = findSkillIndex(filteredSkills, domainPlayerSkill);
                    if (index != -1) {
                        if (isActualSkills) {
                            filteredSkills.get(index).setMaxValue(maxValue);
                            filteredSkills.get(index).setMinValue(minValue);
                        } else {
                            filteredSkills.get(index).setMaxPotentialValue(maxValue);
                            filteredSkills.get(index).setMinPotentialValue(minValue);
                        }
                    } else {
                        FindAuctionsQuery.PlayerSkillFilter querySkill = FindAuctionsQuery.PlayerSkillFilter
                            .builder()
                            .playerSkill(domainPlayerSkill)
                            .build();

                        if (isActualSkills) {
                            querySkill.setMaxValue(maxValue);
                            querySkill.setMinValue(minValue);
                        } else {
                            querySkill.setMaxPotentialValue(maxValue);
                            querySkill.setMinPotentialValue(minValue);
                        }
                        filteredSkills.add(querySkill);
                    }
                } catch (IllegalArgumentException e) {
                    log.error("Invalid skill provided: {}", skillParts[0]);
                }
            }
        }
    }

    private boolean isParsable(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int findSkillIndex(List<FindAuctionsQuery.PlayerSkillFilter> skills, String inputSkill) {
        return IntStream.range(0, skills.size())
            .filter(i -> skills.get(i).getPlayerSkill().equals(inputSkill))
            .findFirst()
            .orElse(-1);
    }
}
