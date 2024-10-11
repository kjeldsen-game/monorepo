import { PlayerOrder } from './PlayerOrder'

export interface MatchReport {
  awayId: string
  homeId: string
  plays: Play[]
  homeScore: number
  awayScore: number
  gameStats: GameStats
}

export interface GameStats {
  homeRating: number
  awayRating: number
}

export interface Play {
  action: Action
  duel: Duel
  clock: number
}

export type Action = 'PASS' | 'POSITION' | 'SHOOT'

export interface Duel {
  type: MatchActionType
  pitchArea: string
  side: MatchEventSide
  initiator: Initiator
  challenger: Initiator | null
  receiver: Receiver | null
  result: MatchResult
  initiatorStats: RStats
  challengerStats: RStats
  origin: Origin
  appliedPlayerOrder: null
}

export interface Initiator {
  id: string
  teamId: string
  name: string
  position: string
  status: Status
  skills: { [key: string]: number }
  playerOrder: PlayerOrder
}

export enum Status {
  Active = 'ACTIVE',
}

export interface RStats {
  total: number
  performance: number
  skillPoints: number
  carryover?: number
  teamAssistance?: { [key: string]: number }
  assistance?: number
}

export enum Origin {
  Default = 'DEFAULT',
}

export interface Receiver {
  id: string
  teamId: string
  name: string
  position: string
  status: Status
  skills: Skills
  playerOrder: PlayerOrder
}

export interface Skills {
  BALL_CONTROL: number
  DEFENSIVE_POSITIONING: number
  PASSING: number
  INTERCEPTING: number
  TACKLING: number
  AERIAL: number
  SCORING: number
  OFFENSIVE_POSITIONING: number
  CONSTITUTION: number
}

export enum MatchResult {
  Lose = 'LOSE',
  Win = 'WIN',
}

export enum MatchActionType {
  Passing = 'PASSING',
  Positional = 'POSITIONAL',
  Tackle = 'TACKLE',
  Shot = 'SHOT',
}

export type MatchEventSide = 'MainEvent' | 'HomeTeamEvent' | 'AwayTeamEvent'
