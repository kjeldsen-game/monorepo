import { Player } from '@/shared/models/player/Player';
import {
  BillboardDeal,
  PlayerWage,
  PriceItem,
  Sponsor,
  Transaction,
} from './Economy';
import { TeamEconomy } from '@/shared/models/player/Team';
import { TeamModifiers } from '@/shared/models/player/TeamModifiers';
import { Cantera, LineupValidationItem } from './Team';

export interface TeamResponse {
  id: string;
  name: string;
  leagueId: string;
  cantera: Cantera;
  economy: TeamEconomy;
  players: Player[];
  teamModifiers: TeamModifiers;
}

export interface LineupValidationResponse {
  valid: boolean;
  items: LineupValidationItem[];
}

export interface EconomyResponse {
  balance: number;
  billboardDeal: BillboardDeal;
  playerWages: PlayerWage[];
  prices: PriceItem[];
  sponsor: Sponsor[];
  transactions: Transaction[];
}
