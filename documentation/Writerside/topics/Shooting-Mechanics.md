# Shooting Mechanics

<primary-label ref="wip"/>

Shooting is the ability of a player to score a goal against the goalkeeper.  
It functions as a duel, using the shooter’s skill and the goalkeeper’s reflexes, along with additional modifiers like **goalkeeper positioning**.

## Types of Shots

### Low Shot (In the Penalty Box)
- **Trigger:** Low pass, successful ball control duel, inside the penalty box
- **Skill Used:** SC (Shooting)
- **Opponent:** Reflexes (goalkeeper)
- **Pitch Area:** Only by forwards in **pitch area 8**
- **Description:**  
  The player uses their **Shooting skill (SC)** against the goalkeeper's **Reflexes** in a duel. The duel occurs in the penalty box after a successful ball control duel.

---

### One on One (With the Goalkeeper)
- **Trigger:** OP > DP (forward is completely free against the goalkeeper)
- **Skill Used:** SC (forward)
- **Opponent Skill Used:** One on One (goalkeeper)
- **Pitch Area:** Only by forwards in **pitch area 8**
- **Description:**  
  The forward has a clear shot on goal. The duel is resolved using the forward’s **Shooting skill** versus the goalkeeper’s **One on One ability**. This is considered a true one-on-one duel.

---

### Header (High Shot)
- **Trigger:** High pass into the box
- **Skill Used:** (AE + SC) / 2 (capped at AE + 10)
- **Opponent:** Reflexes (goalkeeper)
- **Pitch Area:** Only by forwards in **pitch area 8**
- **Description:**  
  A high shot using a combination of **Aerial skill (AE)** and **Shooting skill (SC)** against the goalkeeper’s Reflexes. The effective skill is the average of AE and SC, with a maximum of AE + 10.

---

### Outside Shot
- **Trigger:** Midfield shot with the player order **"Long Shot"**
    - Activation depends on a percentage chance defined by the player order
- **Skill Used:** (PA + SC) / 2 (capped at PA + 10)
- **Opponent:** Reflexes (goalkeeper)
- **Pitch Area:** Midfielders in areas 4, 5, or 6
- **Description:**  
  This shot combines **Passing Ability (PA)** and **Shooting skill (SC)**. The effective skill is the average of PA and SC, capped at PA + 10.
