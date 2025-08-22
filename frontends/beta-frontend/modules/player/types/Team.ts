export interface LineupValidationItem {
  valid: boolean;
  message: string;
}

export interface Cantera {
  score: number;
  economyLevel: number;
  traditionLevel: number;
  buildingsLevel: number;
}
