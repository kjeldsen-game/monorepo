package com.kjeldsen.match.domain.type;

public enum PlayAction {
    PASS, // 2 players same team + duels
    DRIBBLING, // 1 player + duels
    SHOOT, // 1 player + duels
    CORNER, // 2 players same team + duels (for now we deal with it as PASS)
    FAULT // could be SHOOT, PASS or CORNER
}
