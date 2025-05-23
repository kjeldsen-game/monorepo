import { PitchArea } from '../match/PitchArea';
import { PlayerLineupStatus } from './PlayerLineupStatus';
import { PlayerOrder } from './PlayerOrder';
import { PlayerPosition } from './PlayerPosition';
import { TeamRole } from '../MatchReport';

export interface Player {
  id: string;
  age: number;
  name: string;
  teamRole?: TeamRole;
  position?: PlayerPosition;
  prepreferredPosition: PlayerPosition;
  category: Category;
  playerOrder: PlayerOrder;
  playerOrderDestinationPitchArea: PitchArea | any;
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
  Junior = 'JUNIOR',
  Senior = 'SENIOR',
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
  Core = 'CORE',
  Residual = 'RESIDUAL',
  Secondary = 'SECONDARY',
}
