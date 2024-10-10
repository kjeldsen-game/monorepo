package com.kjeldsen.market.rest.mapper;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.player.domain.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AuctionPlayerMapper {
    public Map<Player.PlayerId, Player> mapPlayersById(List<Player> players) {
        return players.stream()
            .collect(Collectors.toMap(Player::getId, player -> player));
    }

    public Map<Auction, Player> mapAuctionsToPlayers(List<Auction> auctions, List<Player> players) {
        Map<Player.PlayerId, Player> playerMap = mapPlayersById(players); // Map players by playerId

        return auctions.stream()
            .collect(Collectors.toMap(
                auction -> auction,
                auction -> playerMap.get(auction.getPlayerId())
            ));
    }
}
