package com.kjeldsen.market.application;


import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.repositories.queries.FindAuctionsQuery;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.queries.FilterMarketPlayersQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetMarketAuctionsUseCase {

    private final PlayerReadRepository playerReadRepository;
    private final AuctionReadRepository auctionReadRepository;

    public Map<Auction, Player> getAuctions(Double maxBid, Double minBid, Integer maxAge, Integer minAge,
            PlayerPosition position, String skills) {
        List<Auction> auctions = auctionReadRepository.findAllByQuery(
            FindAuctionsQuery.builder().auctionStatus(Auction.AuctionStatus.ACTIVE) // TODO use query parameter
                .maxAverageBid(maxBid).minAverageBid(minBid).build());

        List<Player> players = playerReadRepository.filterMarketPlayers(
            FilterMarketPlayersQuery.builder()
                .playerIds(auctions.stream()
                    .map(Auction::getPlayerId)
                    .toList())
                .maxAge(maxAge).minAge(minAge)
                .position(position)
                .skills(parsePlayerSkills(skills))
                .build());

        Map<Player.PlayerId, Player> playerMap = players.stream().collect(
            Collectors.toMap(Player::getId, player -> player));

        return auctions.stream()
            .filter(auction -> playerMap.get(auction.getPlayerId()) != null)
            .collect(Collectors.toMap(
                auction -> auction,
                auction -> playerMap.get(auction.getPlayerId())
            ));
    }


    private List<FilterMarketPlayersQuery.PlayerSkillFilter> parsePlayerSkills(String inputSkills) {
        List<FilterMarketPlayersQuery.PlayerSkillFilter> playerSkills = new ArrayList<>();
        if (inputSkills != null) {
            String[] skills = inputSkills.split(",");
            for (String skill : skills) {
                String[] skillParts = skill.split(":");
                com.kjeldsen.player.domain.PlayerSkill domainPlayerSkill;
                int minValue;
                int maxValue;

                try {
                    domainPlayerSkill = com.kjeldsen.player.domain.PlayerSkill.valueOf(skillParts[0]);

                    minValue = (skillParts.length > 1 && isParsable(skillParts[1]))
                        ? Integer.parseInt(skillParts[1]) : 0;

                    maxValue = (skillParts.length == 3 && isParsable(skillParts[2]))
                        ? Integer.parseInt(skillParts[2]) : 100;
                    playerSkills.add(FilterMarketPlayersQuery.PlayerSkillFilter.builder()
                        .playerSkill(domainPlayerSkill)
                        .maxValue(maxValue)
                        .minValue(minValue)
                        .build());
                } catch (IllegalArgumentException e) {
                    log.error("Invalid skill provided: {}", skillParts[0]);
                }
            }
        }
        return playerSkills;
    }

    private boolean isParsable(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
