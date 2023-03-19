package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.CreatePlayerUseCase;
import com.kjeldsen.player.application.usecases.GeneratePlayersUseCase;
import com.kjeldsen.player.domain.NewPlayer;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.TeamId;
import com.kjeldsen.player.domain.repositories.FindPlayersQuery;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.rest.api.PlayerApiDelegate;
import com.kjeldsen.player.rest.mapper.PlayerMapper;
import com.kjeldsen.player.rest.model.CreatePlayerRequest;
import com.kjeldsen.player.rest.model.GeneratePlayersRequest;
import com.kjeldsen.player.rest.model.PlayerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PlayersDelegate implements PlayerApiDelegate {

    private final CreatePlayerUseCase createPlayerUseCase;
    private final GeneratePlayersUseCase generatePlayersUseCase;
    private final PlayerReadRepository playerReadRepository;

    @Override
    public ResponseEntity<Void> createPlayer(CreatePlayerRequest createPlayerRequest) {
        NewPlayer newPlayer = NewPlayer.builder()
            .age(PlayerAge.of(createPlayerRequest.getAge()))
            .position(PlayerPosition.valueOf(createPlayerRequest.getPosition().name()))
            .points(createPlayerRequest.getPoints())
            .teamId(TeamId.of("NOTEAM"))
            .build();
        createPlayerUseCase.create(newPlayer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<PlayerResponse>> generatePlayer(GeneratePlayersRequest generatePlayersRequest) {
        List<Player> players = generatePlayersUseCase.generate(generatePlayersRequest.getNumberOfPlayers(), TeamId.of("NOTEAM"));
        List<PlayerResponse> response = players.stream().map(PlayerMapper.INSTANCE::map).toList();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<PlayerResponse>> getAllPlayers(com.kjeldsen.player.rest.model.PlayerPosition position, Integer size, Integer page) {
        FindPlayersQuery query = FindPlayersQuery.builder()
            .position(position != null ? PlayerPosition.valueOf(position.name()) : null)
            .size(size)
            .page(page)
            .build();
        List<Player> players = playerReadRepository.find(query);
        List<PlayerResponse> response = players.stream().map(PlayerMapper.INSTANCE::map).toList();
        return ResponseEntity.ok(response);
    }
}
