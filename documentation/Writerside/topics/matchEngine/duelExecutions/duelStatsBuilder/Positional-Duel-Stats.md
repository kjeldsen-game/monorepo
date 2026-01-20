# Positional Duel Stats

This process is responsible for building duel statistics exclusively for positional duels.

## Steps

1. Retrieve the player’s **skill points** based on the positional duel type and player role.

2. Generate a **random Gaussian performance value**.

3. Retrieve and apply the **chain action modifier**.

4. Retrieve and apply **assistance values** and **assistance modifiers**.

5. Calculate the final duel stats as the sum of:
    - Skill points
    - Chain action modifier
    - Performance total
    - Adjusted assistance
    - Assistance modifiers total

6. Return the **calculated positional duel statistics**.

## Flowchart Overview

```mermaid
flowchart LR
    B[Get Skill Points </br> for Positional Duel & Role]
    C[Generate Random Gaus </br> Performance Value]
    D[Apply Chain Action Modifier]
    E[Apply Assistance &\nAssistance Modifiers]
    F[Calculate Total Duel Stats]
    G[Return Positional Duel Stats]

    B --> C
    C --> D
    D --> E
    E --> F
    F --> G

```