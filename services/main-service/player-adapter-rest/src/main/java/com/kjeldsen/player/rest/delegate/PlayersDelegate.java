package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.lib.events.AuctionCreationEvent;
import com.kjeldsen.lib.publishers.GenericEventPublisher;
import com.kjeldsen.player.application.usecases.CreatePlayerUseCase;
import com.kjeldsen.player.application.usecases.GeneratePlayersUseCase;
import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.application.usecases.player.PlayerSellUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Team.TeamId;
import com.kjeldsen.player.domain.repositories.queries.FindPlayersQuery;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.rest.api.PlayerApiDelegate;
import com.kjeldsen.player.rest.mapper.CreatePlayerMapper;
import com.kjeldsen.player.rest.mapper.PlayerMapper;
import com.kjeldsen.player.rest.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class PlayersDelegate implements PlayerApiDelegate {

    private final CreatePlayerUseCase createPlayerUseCase;
    private final GeneratePlayersUseCase generatePlayersUseCase;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerSellUseCase playerSellUseCase;
    private final GetTeamUseCase getTeamUseCase;
//    private final AuctionCreationEventPublisher auctionCreationEventPublisher;
    private final GenericEventPublisher auctionCreationEventPublisher;

    @Override
    public ResponseEntity<Void> createPlayer(CreatePlayerRequest createPlayerRequest) {
        Team team = getTeamUseCase.get(SecurityUtils.getCurrentUserId());
        CreatePlayerUseCase.NewPlayer newPlayer = CreatePlayerMapper.INSTANCE.map(createPlayerRequest);

        newPlayer.setTeamId(team.getId());
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
    public ResponseEntity<List<PlayerResponse>> getAllPlayers(PlayerStatus status, String teamId, PlayerPosition position, Integer size, Integer page) {
//        if (teamId == null) {
//            Team team = getTeamUseCase.get(SecurityUtils.getCurrentUserId());
//            teamId = team.getId().value();
//        }

        FindPlayersQuery query = FindPlayersQuery.builder()
            .position(position != null ? com.kjeldsen.player.domain.PlayerPosition.valueOf(position.name()) : null)
            .teamId(TeamId.of(teamId))
            .status(status != null ? com.kjeldsen.player.domain.PlayerStatus.valueOf(status.name()) : null)
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

    @Override
    public ResponseEntity<Void> sellPlayer(String playerId) {
        Optional<Player> optionalPlayer = playerReadRepository.findOneById(Player.PlayerId.of(playerId));
        if (optionalPlayer.isPresent() && !optionalPlayer.get().getTeamId().equals(
            getTeamUseCase.get(SecurityUtils.getCurrentUserId()).getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        auctionCreationEventPublisher.publishEvent(
            playerSellUseCase.sell(Player.PlayerId.of(playerId)));
        return ResponseEntity.ok().build();
    }
}
