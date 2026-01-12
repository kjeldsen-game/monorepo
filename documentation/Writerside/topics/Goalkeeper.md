# Goalkeepers

<primary-label ref="wip"/>

Goalkeepers stop the ball from going into the net, primarily using their **Reflexes (RE)**, but also rely on other skills to perform effectively.

---

## Reflexes (RE)
- **Effect:** Primary skill used to stop most shots
- **Role in Shot Duel:** Acts as the counter to shot quality
- **Random Impact:** Affected by randomness
- **Carryover Assistance:** Can inherit positive assistance from the defender, effectively acting as a third duel. If the defender’s assistance is positive, RE inherits that bonus

---

## Goalkeeper Positioning (GP)
- **Effect:** Represents the ability of the goalkeeper to be in the right place at the right time before attempting a save
- **Bonus/Malus:** Provides a bonus or penalty to Reflexes
- **Mechanic:** Dice roll-based system
    - **Dice Roll:** 0–100 determines success
    - **Success:** If GP > dice roll, goalkeeper successfully positions himself
        - Bonus = GP − Dice Roll (added to RE, diminished by RDR)
    - **Failure:** If GP < dice roll, goalkeeper fails to position properly
        - Malus = GP − Dice Roll applied to RE

**Example:**
- GP = 75, Dice Roll = 60 → Success → +15 to RE
- GP = 55, Dice Roll = 80 → Fail → −25 to RE

---

## Interceptions (IN)
- **Effect:** Ability to intercept high passes
- **Random Factor:**
    - 0–30 → No interception
    - 30–50 → 10% chance
    - 50–75 → 25% chance
    - 75+ → 50% chance
- **Outcome:** If intercepted, goalkeeper stops the pass and gains possession, starting the next play

---

## Control (CT)
- **Effect:** Ability to securely hold the ball and prevent fumbling
- **Random Factor:**
    - 0–30 → No issue
    - 30–50 → 25% chance of fumble
    - 50–75 → 10% chance of fumble
    - 75+ → 0% chance of fumble
- **Outcome:** If the ball is fumbled, it goes into the net, resulting in a goal

---

## Organisation (ORG)
- **Effect:** Goalkeeper’s ability to interact with the defense
- **Functions:**
    1. Adds to **defensive assistance** whenever a defender is the active defender (not affected by randomness)
    2. Determines ability to start the next play by making a forward pass (affected by randomness)

---

## One on One (1v1)
- **Effect:** Ability to handle one-on-one situations with attackers
- **Condition:** Occurs when a forward wins the Positioning Duel with a clear margin
- **Effect on Duel:** Uses **One on One** skill instead of Reflexes
- **Outcome:** The duel resolves like other duels, with the goalkeeper’s One on One skill determining the result
