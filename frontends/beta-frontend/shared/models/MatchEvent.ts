import { PitchArea } from './PitchArea'

export type MatchEventSide = 'MainEvent' | 'HomeTeamEvent' | 'AwayTeamEvent'

export type MatchActionType = 'PASS' | 'POSITION' | 'TACKLE' | 'SHOOT'

export interface PlayerEventStats {
  playerName: string
  skillContribution?: number
  performance?: number
  assistance?: number
  total?: number
  carryOver?: number
}

export interface MatchEvent {
  clock: number
  area: PitchArea
  action: MatchActionType
  eventStart: string
  eventResponse: string
  eventResult: string
  eventSide: MatchEventSide
  actionStats: {
    player1: PlayerEventStats
    player2: PlayerEventStats
  }
}
