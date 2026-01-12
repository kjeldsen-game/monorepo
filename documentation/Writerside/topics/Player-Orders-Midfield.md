# Player Orders - Midfield

<primary-label ref="wip"/>

## Pass Forward
- **Effect:** Moves the ball from midfield to forward positions, advancing play into more attacking areas.

## Long Shot
- **Effect:** Attempts a shot directly from midfield
    - If on the **flank:** +25 to goalkeeper’s chances (GP)
    - If in the **center:** +10 to goalkeeper’s chances (GP)

## Change Flank
- **Effect:** Switches the ball from one flank to the other
    - **50% chance** the receiver is the Fullback (FB)
    - **75% chance** the receiver is the Wingback (WB)

## Pass to Area
- **Effect:** The midfielder moves the ball horizontally to any of the midfield areas.
    - No bonus or penalty, just repositioning
- **Representation:** Can be shown with three options in a dropdown, or an optional secondary dropdown for selecting the area
- **Applicable to:** Only midfielders
- **Parameter:** The destination area must be represented

## Dribble to Area
- **Effect:** The player attempts to change to an adjacent area or move forward (never backward)
    - If successful, the player faces the opponent at the new location
    - No bonus or penalty is applied
- **Representation:** Same as "Pass to Area," with destination area as a parameter
- **Duels Used:** Positioning Duel and Ball Control Duel

## Wall Pass
- **Effect:** A combination between two players to create a better attacking chance
    - A player passes the ball and moves toward the attack
    - The receiver controls the ball without any penalty or bonus
- **Result:** If successful, the pass will impact the original player’s **Opportunity (OP)** linearly
    - **PA = 100** → -15 DP
    - **PA = 0** → +15 DP
