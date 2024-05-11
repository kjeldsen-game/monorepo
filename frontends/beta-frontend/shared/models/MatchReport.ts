import { MatchEvent } from './MatchEvent'
import { PlayerPosition } from './PlayerPosition'

export interface MatchReportPlayer {
  name: string
  position: PlayerPosition
}

export interface MatchReportType {
  homePlayers: MatchReportPlayer[]
  awayPlayers: MatchReportPlayer[]
  events: MatchEvent[]
}
