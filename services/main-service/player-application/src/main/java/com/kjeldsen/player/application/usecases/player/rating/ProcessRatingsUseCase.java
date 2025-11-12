package com.kjeldsen.player.application.usecases.player.rating;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.provider.RatingProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.utils.RatingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProcessRatingsUseCase {

    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;

    public void process() {
//        List<Player> players = playerReadRepository.findAll();
//        players.forEach(player -> {
//              switch (player.getPreferredPosition()) {
//                  case FORWARD, STRIKER -> RatingProvider.getRating(player, RatingUtils.FORWARD_RATING_MAP);
//                  case LEFT_WINGER, OFFENSIVE_MIDFIELDER, RIGHT_WINGER -> RatingProvider.getRating(player, RatingUtils.WINGER_RATING_MAP);
//                  case CENTRE_MIDFIELDER, LEFT_MIDFIELDER, RIGHT_MIDFIELDER ->  RatingProvider.getRating(player, RatingUtils.MIDFIELD_RATING_MAP);
//                  case DEFENSIVE_MIDFIELDER, LEFT_WINGBACK, RIGHT_WINGBACK -> RatingProvider.getRating(player, RatingUtils.WINGBACK_RATING_MAP);
//                  case CENTRE_BACK, LEFT_BACK, RIGHT_BACK -> RatingProvider.getRating(player, RatingUtils.BACK_RATING_MAP);
//                  case GOALKEEPER ->  RatingProvider.getRating(player, RatingUtils.GOALKEEPER_RATING_MAP);
//                  default -> throw new IllegalStateException("Unexpected value: " + player.getPreferredPosition());
//              }
//        });

//        playerWriteRepository.saveAll(players);
    }
}
