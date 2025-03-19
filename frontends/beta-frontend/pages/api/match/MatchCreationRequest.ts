export interface MatchCreationRequest {
  home: Away;
  away: Away;
}

export interface Away {
  id: string;
  // modifiers: Modifiers
}

export interface Modifiers {
  tactic: string;
  horizontalPressure: string;
  verticalPressure: string;
}
