import { Rating } from 'modules/player/types/Player';
import { Player } from './Player';
import { TeamModifiers } from './TeamModifiers';

export interface Team {
  id: string;
  name: string;
  cantera: Cantera;
  economy: TeamEconomy;
  players: Player[];
  teamModifiers: TeamModifiers;
  rating: Rating
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

export interface TeamFormationValiation {
  valid: boolean;
  items: TeamFormationValidationItem[];
}

export interface TeamFormationValidationItem {
  valid: boolean;
  message: string;
}
