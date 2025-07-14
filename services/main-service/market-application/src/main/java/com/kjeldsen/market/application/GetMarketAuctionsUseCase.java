package com.kjeldsen.market.application;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.repositories.queries.FindAuctionsQuery;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.queries.FilterMarketPlayersQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetMarketAuctionsUseCase {

    private final PlayerReadRepository playerReadRepository;
    private final AuctionReadRepository auctionReadRepository;

    public Map<Auction, Player> getAuctions(Double maxBid, Double minBid, Integer maxAge, Integer minAge,
            PlayerPosition position, String skills, String potentialSkills, String playerId) {

        log.info("GetMarketAuctionsUseCase for maxBid = {}, minBid = {}, maxAge = {} minAge = {}" +
                " position = {} skills = {} potentialSkills = {} playerId = {}", maxBid, minBid, maxAge, minAge,
                position, skills, potentialSkills, playerId);

        List<Auction> auctions = auctionReadRepository.findAllByQuery(
                FindAuctionsQuery.builder().auctionStatus(Auction.AuctionStatus.ACTIVE)
                        .maxAverageBid(maxBid).minAverageBid(minBid).playerId(Player.PlayerId.of(playerId)).build());

        System.out.println(auctions.size());

        List<Player> players = playerReadRepository.filterMarketPlayers(
                FilterMarketPlayersQuery.builder()
                        .playerIds(auctions.stream()
                                .map(Auction::getPlayerId)
                                .toList())
                        .maxAge(maxAge).minAge(minAge)
                        .preferredPosition(position)
                        .skills(parsePlayerSkills(skills, potentialSkills))
                        .build());

        System.out.println(players.size());

        Map<Player.PlayerId, Player> playerMap = players.stream().collect(
                Collectors.toMap(Player::getId, player -> player));

        log.info("Player map : {}", playerMap);

        return auctions.stream()
                .filter(auction -> playerMap.get(Player.PlayerId.of(auction.getPlayerId())) != null)
                .collect(Collectors.toMap(
                        auction -> auction,
                        auction -> playerMap.get(Player.PlayerId.of(auction.getPlayerId()))));
    }

    private List<FilterMarketPlayersQuery.PlayerSkillFilter> parsePlayerSkills(
            String actualSkillsString, String potentialSkillsString) {
        List<FilterMarketPlayersQuery.PlayerSkillFilter> playerSkills = new ArrayList<>();
        parseSkillString(playerSkills, actualSkillsString, true);
        parseSkillString(playerSkills, potentialSkillsString, false);
        System.out.println(playerSkills);
        return playerSkills;
    }

    private void parseSkillString(
            List<FilterMarketPlayersQuery.PlayerSkillFilter> filteredSkills, String skillString,
            boolean isActualSkills) {
        if (skillString != null && !skillString.isEmpty()) {
            String[] skills = skillString.split(",");
            for (String skill : skills) {
                String[] skillParts = skill.split(":");
                com.kjeldsen.player.domain.PlayerSkill domainPlayerSkill;
                int minValue;
                int maxValue;

                try {
                    domainPlayerSkill = com.kjeldsen.player.domain.PlayerSkill.valueOf(skillParts[0]);

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
                        FilterMarketPlayersQuery.PlayerSkillFilter querySkill = FilterMarketPlayersQuery.PlayerSkillFilter
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

    private int findSkillIndex(List<FilterMarketPlayersQuery.PlayerSkillFilter> skills, PlayerSkill inputSkill) {
        return IntStream.range(0, skills.size())
                .filter(i -> skills.get(i).getPlayerSkill().name().equals(inputSkill.name()))
                .findFirst()
                .orElse(-1);
    }
}
