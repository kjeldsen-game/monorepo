import { PitchArea } from './PitchArea'

export type MatchEventType = 'MainEvent' | 'HomeTeamEvent' | 'AwayTeamEvent'

export type MatchActionType = 'PASS' | 'POSITION' | 'TACKLE' | 'SHOOT'

export interface PlayerEventStats {
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
  actionStats: {
    player1: PlayerEventStats
    player2: PlayerEventStats
  }
}
