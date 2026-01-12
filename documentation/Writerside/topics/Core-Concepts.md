# Core Concepts

<primary-label ref="wip"/>

## Continuous Flow
Kjeldsen operates on a continuous flow system, where the game unfolds in a seamless sequence of actions.
Following kickoff, the initial team in possession begins an "opportunity" – a chain of events culminating in a goal, 
a tackle, or the ball going out of bounds. Each action within an opportunity takes approximately 15 seconds to complete, 
creating a dynamic and fluid gameplay experience. This continuous flow system allows players to directly influence the 
game's tempo. More aggressive tactics and skillful players can generate a higher frequency of opportunities, while more 
cautious approaches may result in a slower, more methodical style of play.

## Attacking & Defending Teams
At any given moment, one team is actively attacking while the other defends. This is important to read the match engine 
in the right key: all the actions are looked at from an attacker's perspective trying to overcome the defender.

## Active & Supporting Players
Each action has two sides:

### Offence
- an "active" offensive player (e.g., the player currently in possession)
- a group of "supporting" players who provide assistance

### Defence
- an "active" defensive player (e.g., the player currently challenging the attacker)
- a group of "supporting" players who provide assistance

## Pitch Area
The playing field is divided into 9 distinct positions. Player movement is restricted, allowing them to move only one
or two positions per action. This positional restriction significantly impacts a player's ability to perform certain
actions. For example, a midfielder will have a significantly reduced chance of scoring from a deep-lying position.

## Action & Action Components
An action represents a single unit of gameplay, encompassing a series of duels and culminating in a specific outcome.

### Duel Sequence
Every action within the game is resolved through a series of duels:

#### Positioning Duel
This determines the positional advantage between the active attacker and the active defender. Modifiers such as player 
personalities, team instructions, and random factors influence the outcome. A successful positioning duel for the attacker
grants them freedom of movement.

#### Ball Control Duel
This pits the attacker's ball control skills against the defender's tackling ability. A successful ball control duel 
allows the attacker to maintain possession and continue the opportunity.

##### Shot Duel
(When applicable) This occurs after a successful positioning and ball control duel, pitting the attacker's scoring ability
against the goalkeeper's shot-stopping ability.

#### Action Outcome
The outcome of an action can vary significantly:

#### Types
- **Pass**: The attacking team maintains possession, with the ball moving to a different player.  
  Passes can be forward, to a pitch area in an advanced zone, or sideways, a pass to another player in the same zone.
- **Dribble**: The attacking player progresses with the ball, moving into a more advantageous position.
- **Shot on Goal**: The attacker attempts to score, triggering the Shot Duel.
- **Tackle**: The defending team successfully dispossesses the attacker.

#### Modifiers

- **Position**: The attacker's current position on the field heavily influences their available actions. Forwards are more likely to shoot, while midfielders are more inclined to pass or dribble forward. Defenders typically focus on passing to teammates or playing safe passes.
- **Pitch Area**: An attacker's position within their designated area also plays a crucial role. Players in advanced positions are more likely to attempt shots or crosses, while those in deeper positions are more likely to pass or look for longer, more ambitious passes.
- **Player Orders**: Managers can issue specific instructions to individual players, influencing their decision-making during gameplay. For example, a midfielder can be instructed to "Pass Short," "Dribble Forward," or "Shoot on Sight." These orders will significantly impact the attacker's actions, but their effectiveness will depend on their current position on the field.

## Opportunity
An "Opportunity" represents a continuous sequence of actions undertaken by a single team while in possession of the ball. It begins with an initial action (e.g., a kickoff, a won tackle) and continues until one of the following occurs:
- Goal: The attacking team scores.
- Turnover: The defending team successfully dispossesses the attacking team.
- Action Limit Exceeded: The attacking team exceeds the limit of consecutive passes in the same direction or backwards (typically 3 passes).

## Dangerous Opportunities
Dangerous Opportunities are defined as situations where the attacking team poses an immediate threat to score. This includes scenarios such as:
- Shots on Goal: Any attempt by an attacker to score.
- Dangerous Crosses: Crosses delivered into the penalty area with a high probability of resulting in a goal or a scoring chance.
- Through Balls: Passes played through the defense with the intent of releasing a teammate for a clear scoring opportunity.
