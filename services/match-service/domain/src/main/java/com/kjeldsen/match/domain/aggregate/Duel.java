package com.kjeldsen.match.domain.aggregate;

public class Duel {

    private DuelAttacker attacker;
    private DuelDefender defender;
    private DuelType type;
    // Always from the attacker point of view
    private DuelResult result;

}
