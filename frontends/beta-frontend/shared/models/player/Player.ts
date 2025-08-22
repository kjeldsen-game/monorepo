import { PlayerLineupStatus } from './PlayerLineupStatus';
import { PlayerOrder } from './PlayerOrder';
import { PlayerPosition } from './PlayerPosition';
import { TeamRole } from '../MatchReport';
import { PitchArea } from 'modules/player/types/Player';

export interface Player {
  id: string;
  age: PlayerAge;
  name: string;
  teamRole?: TeamRole;
  position?: PlayerPosition;
  preferredPosition: PlayerPosition;
  category: Category;
  playerOrder: PlayerOrder;
  playerOrderDestinationPitchArea: PitchArea;
  status: PlayerLineupStatus;
  actualSkills: { [key: string]: ActualSkill };
  economy: PlayerEconomy;
}

export interface PlayerAge {
  years: number;
  months: number;
  days: number;
}

export enum Category {
  JUNIOR = 'JUNIOR',
  SENIOR = 'SENIOR',
}

export interface PlayerEconomy {
  salary: number;
}

export interface ActualSkill {
  PlayerSkills: PlayerSkills;
}

export interface PlayerSkills {
  actual: number;
  potential: number;
  playerSkillRelevance: PlayerSkillRelevance;
}

export enum PlayerSkillRelevance {
  CORE = 'CORE',
  RESIDUAL = 'RESIDUAL',
  SECONDARY = 'SECONDARY',
}
