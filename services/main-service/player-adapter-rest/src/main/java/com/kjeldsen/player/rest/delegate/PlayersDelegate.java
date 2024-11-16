package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.player.application.usecases.CreatePlayerUseCase;
import com.kjeldsen.player.application.usecases.GeneratePlayersUseCase;
import com.kjeldsen.player.application.usecases.player.PlayerSellUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Team.TeamId;
import com.kjeldsen.player.domain.publishers.AuctionCreationEventPublisher;
import com.kjeldsen.player.domain.repositories.FindPlayersQuery;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.rest.api.PlayerApiDelegate;
import com.kjeldsen.player.rest.mapper.CreatePlayerMapper;
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
    private final PlayerSellUseCase playerSellUseCase;
    private final AuctionCreationEventPublisher auctionCreationEventPublisher;

    @Override
    public ResponseEntity<Void> createPlayer(CreatePlayerRequest createPlayerRequest) {
        CreatePlayerUseCase.NewPlayer newPlayer = CreatePlayerMapper.INSTANCE.map(createPlayerRequest);
        newPlayer.setTeamId(Team.TeamId.of("NOTEAM")); // TODO change to receive team id in api?
        createPlayerUseCase.create(newPlayer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<PlayerResponse>> generatePlayer(GeneratePlayersRequest generatePlayersRequest) {
        List<Player> players = generatePlayersUseCase.generate(generatePlayersRequest.getNumberOfPlayers(), Team.TeamId.of("NOTEAM"));
        List<PlayerResponse> response = players.stream().map(PlayerMapper.INSTANCE::playerResponseMap).toList();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<PlayerResponse>> getAllPlayers(com.kjeldsen.player.rest.model.PlayerPosition position, Integer size, Integer page) {
        final String currentUserId = SecurityUtils.getCurrentUserId();
        TeamId teamId = TeamId.of(currentUserId);
        FindPlayersQuery query = FindPlayersQuery.builder()
            .position(position != null ? PlayerPosition.valueOf(position.name()) : null)
            .teamId(teamId)
            .size(size)
            .page(page)
            .build();
        List<Player> players = playerReadRepository.find(query);
        List<PlayerResponse> response = players.stream().map(PlayerMapper.INSTANCE::playerResponseMap).toList();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PlayerResponse> getPlayerById(String playerId) {
        Player player = playerReadRepository.findOneById(Player.PlayerId.of(playerId))
            .orElseThrow();
        PlayerResponse response = PlayerMapper.INSTANCE.playerResponseMap(player);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Void> sellPlayer(String playerId) {
        auctionCreationEventPublisher.publishAuctionCreationEvent(
            playerSellUseCase.sell(Player.PlayerId.of(playerId)));
        return ResponseEntity.ok().build();
    }
}
