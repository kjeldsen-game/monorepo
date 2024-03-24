export interface Match {
  id: string
  home: MatchTeam
  away: MatchTeam
  dateTime: Date
  status: MatchStatus
}

export type MatchStatus = 'PENDING' | 'ACCEPTED' | 'REJECTED'

export interface MatchTeam {
  id: string
  modifiers: Modifiers
}

export interface Modifiers {
  tactic: string
  horizontalPressure: string
  verticalPressure: string
}
