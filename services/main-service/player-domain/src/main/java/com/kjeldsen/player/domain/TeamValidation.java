package com.kjeldsen.player.domain;

import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;

import java.util.Optional;

public class TeamValidation {

    PlayerReadRepository playerReadRepository;
    TeamReadRepository teamReadRepository;
    public boolean isReadyToMatch(Player.PlayerId playerId, PlayerPosition position, Team team){

        if (){
            throw new IllegalArgumentException("Your team must have 11 players");
        }
        //* Defense
        Optional<Player> player = playerReadRepository.findOneById(playerId);

        if (){
            throw new IllegalArgumentException("Your team must have 3 to 5 defenders.");
        }
        //* Sweeper
        if (){
            throw new IllegalArgumentException("Your team must have 0 to 1 sweeper.");
        }
        //* Midfields
        if (){
            throw new IllegalArgumentException("Your team must have 3 to 5 midfields.");
        }
        //* Forward
        if (){
            throw new IllegalArgumentException("Your team must have 1 to 3 forwards.");
        }
        //* Striker
        if (){
            throw new IllegalArgumentException("Your team must have at maximum one striker.");
        }
        //* Goalkeeper
        if (){
            throw new IllegalArgumentException("Your team must have a goalkeeper.");
        }

        return true;
    };

    private boolean isDefenseOk(){
        return true;
    }
}
