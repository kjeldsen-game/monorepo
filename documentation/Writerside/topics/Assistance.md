# Assistance: Team Support in Duels

<primary-label ref="wip"/>

**Assistance** represents the influence of a player's teammates during a duel.  
In any duel, two players are directly involved:

- **Active Attacker**
- **Active Defender**

The remaining players provide support to their respective active teammate based on their offensive or defensive skills. Assistance is calculated by comparing the summed contributions of all supporting players and applying the resulting difference as a bonus to the active players.

---

## Key Mechanics

### Skill Contribution

#### Offensive Skills
- **Offensive Positioning (OP):** 100% of the value
- **Ball Control (BC):** 90% of the value

#### Defensive Skills
- **Defensive Positioning (DP):** 100% of the value
- **Tackling Ability (TA):** 90% of the value

---

### Support Calculation
1. Sum the total **offensive support** and total **defensive support** from the supporting players.
2. Subtract the defensive total from the offensive total to determine the net assistance:

