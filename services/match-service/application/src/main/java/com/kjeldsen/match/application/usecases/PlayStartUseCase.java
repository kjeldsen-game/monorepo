package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.PlayAction;
import com.kjeldsen.match.domain.PlayType;
import com.kjeldsen.match.domain.aggregate.Duel;
import com.kjeldsen.match.domain.aggregate.Match;
import com.kjeldsen.match.domain.aggregate.Player;
import com.kjeldsen.match.domain.events.PlayStartedEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class PlayStartUseCase {

    public void startPlay(
        String matchId,
        ImmutablePair<Player, Player> players,
        PlayAction playAction,
        List<Duel> duels
    ) {
        // TODO add validation if the match didn't start a play can not be started
        // TODO log which players are in the play


        Match match = Match.builder().matchId(matchId).build(); // TODO replace this line by a search in the DB

        if (match.isInitialPlay()) {
            addInitialPlay(match);
        } else {
            addNextPlay(match, playAction, duels);
        }
    }

    private void addNextPlay(Match match, PlayAction playAction, List<Duel> duels) {

        // TODO do we need to check something in the previous play?

        PlayStartedEvent.builder()
            .eventId("adsd")
            .date(Instant.now())
            .type(PlayType.NORMAL)
            .matchId(match.getMatchId())
            .action(playAction)
            .duels(duels)
            .build();
    }

    private void addInitialPlay(Match match) {

        // TODO validate initial play should not have duels

        PlayStartedEvent.builder()
            .eventId("adsd")
            .date(Instant.now())
            .type(PlayType.INITIAL)
            .action(PlayAction.PASS)
            .matchId(match.getMatchId())
            .build();
    }

}
