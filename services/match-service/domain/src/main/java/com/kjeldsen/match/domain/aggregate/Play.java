package com.kjeldsen.match.domain.aggregate;

import com.kjeldsen.match.domain.id.PlayId;
import lombok.Getter;

import java.util.List;

@Getter
public class Play {

    private PlayId playId;
    private List<Player> players;
    private List<Duel> duels;

}