import { type UUID } from 'crypto'

type VERTICAL = 'LEFT' | 'CENTER' | 'RIGHT'
type HORIZONTAL = 'BACK' | 'MID' | 'FORWARD'

type XAxis = 0 | 1 | 2 | 3 | 4
type YAxis = 0 | 1 | 2

export type PLAY_DIRECTION = 'FORWARDS' | 'BACKWARDS'
export type Position = `${VERTICAL}-${HORIZONTAL}` | 'GOALS' | 'BENCH'
/*
  DIRECTION FORWARDS (Player Perspective)
  .   LB   LM   LF  .
  GD  CB   CM   CF  GA
  .   RB   RM   RF  .
  DIRECTION BACKWARDS (Competitor Perspective)
  .   RF   RM   RB  .
  GA  CF   CM   CB  GD
  .   LF   LM   LB  .
  NORMALIZED POSITION
  [0,0] [1,0] [2,0] [3,0] [4,0] 
  [0,1] [1,1] [2,1] [3,1] [4,1]
  [0,2] [1,2] [2,2] [3,2] [4,2]
*/
export type NormalizedPostion = [XAxis, YAxis]

export type PlayStyle =
  | 'GOAL-KEEPER'
  | 'GOAL-KEEPER-SWEEPER'
  | 'DEFENDER-SWEEPER'
  | 'DEFENDER-CENTERAL-BACK'
  | 'DEFENDER-FULL-BACK'
  | 'DEFENDER-WING-BACK'
  | 'MID-FIELDER-DEFENSIVE'
  | 'MID-FIELDER-WIDE'
  | 'MIDFIELDER-CENTRAL'
  | 'ATTACKING-MID-FIELDER'
  | 'FORWARD-CENTER'
  | 'FORWARD-WINGER'

export interface IPlayer {
  id: UUID
  name: string
  position: NormalizedPostion
  playStyle: PlayStyle
  startingPosition: Position
}

export interface ITeam {
  id: UUID
  name: string
  players: IPlayer[]
}

export interface IMatch {
  id: UUID
  homeTeam: ITeam
  awayTeam: ITeam
}
