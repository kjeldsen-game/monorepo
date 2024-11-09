import { Player } from './Player';

export interface Team {
  id: string;
  name: string;
  cantera: Cantera;
  economy: TeamEconomy;
  players: Player[];
}

export interface Cantera {
  score: number;
  economyLevel: number;
  traditionLevel: number;
  buildingsLevel: number;
}

export interface TeamEconomy {
  balance: number;
}
