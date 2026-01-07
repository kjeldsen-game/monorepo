package com.kjeldsen.match.application.mappers;

import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.rest.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PlayerMapperTest {

    @Test
    @DisplayName("Should map EditPlayerRequest list to Player list")
    void should_map_EditPlayerRequest_list_to_Player_list() {
        EditPlayerRequest editPlayerRequest = new EditPlayerRequest()
            .id("id")
            .playerOrder(PlayerOrder.NONE)
            .playerOrderDestinationPitchArea(PitchArea.CENTRE_BACK)
            .status(PlayerStatus.ACTIVE)
            .position(PlayerPosition.FORWARD);

        List<Player> players = PlayerMapper.INSTANCE.mapEditPlayerList(List.of(editPlayerRequest));
        System.out.println(players);
    }

    @Test
    @DisplayName("Should map PlayerResponse list to Player list")
    void should_map_PlayerResponse_list_to_Player_list() {
        com.kjeldsen.player.rest.model.PlayerResponse playerResponse = new com.kjeldsen.player.rest.model.PlayerResponse()
            .id("id")
            .playerOrder(com.kjeldsen.player.rest.model.PlayerOrder.NONE)
            .playerOrderDestinationPitchArea(com.kjeldsen.player.rest.model.PitchArea.CENTRE_BACK)
            .status(com.kjeldsen.player.rest.model.PlayerStatus.ACTIVE)
            .position(com.kjeldsen.player.rest.model.PlayerPosition.FORWARD);

        List<Player> players = PlayerMapper.INSTANCE.mapPlayerResponseList(List.of(playerResponse));
        System.out.println(players);
    }
}
