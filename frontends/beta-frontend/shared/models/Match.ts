export interface Match {
  home: MatchTeam
  away: MatchTeam
  dateTime: Date
}

export interface MatchTeam {
  id: string
  modifiers: Modifiers
}

export interface Modifiers {
  tactic: string
  horizontalPressure: string
  verticalPressure: string
}
