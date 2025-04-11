package com.kjeldsen.player.test.mapper;

import com.kjeldsen.player.application.usecases.CreatePlayerUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.rest.mapper.CreatePlayerMapper;
import com.kjeldsen.player.rest.model.CreatePlayerRequest;

import com.kjeldsen.player.rest.model.PlayerPosition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreatePlayerMapperTest {

    @Test
    void should_find_input_and_return_expected_value_between_createPlayerRequest_and_createPlayerUseCase() {
        Integer points = 50;

        PlayerPosition position = PlayerPosition.valueOf("CENTRE_BACK");

        CreatePlayerRequest createPlayerRequest = new CreatePlayerRequest();
        createPlayerRequest.setPoints(points);
        createPlayerRequest.setPosition(position);

        CreatePlayerUseCase.NewPlayer player = CreatePlayerMapper.INSTANCE.map(createPlayerRequest);

        assertEquals(points, player.getPoints());
        assertEquals(position.toString(), player.getPosition().toString());
    }

    @Test
    void should_extract_string_from_playerId_value_and_be_the_same() {

        Player.PlayerId playerId = Player.PlayerId.generate();
        String playerIdDirectString = CreatePlayerMapper.INSTANCE.map(playerId);
        assertEquals(playerId.value(), playerIdDirectString);
    }
}
