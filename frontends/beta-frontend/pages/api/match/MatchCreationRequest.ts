export interface MatchCreationRequest {
  home: Away
  away: Away
  dateTime: Date
}

export interface Away {
  id: string
  modifiers: Modifiers
}

export interface Modifiers {
  tactic: string
  horizontalPressure: string
  verticalPressure: string
}
