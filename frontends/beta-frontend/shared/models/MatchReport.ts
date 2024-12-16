import { PlayerOrder } from './PlayerOrder';

export interface MatchReport {
  awayId: string;
  homeId: string;
  plays: Play[];
  homeScore: number;
  awayScore: number;
  gameStats: GameStats;
}

export interface GameStats {
  homeRating: number;
  awayRating: number;
}

export interface Play {
  homeScore: number;
  awayScore: number;
  action: Action;
  duel: Duel;
  clock: number;
}

export type Action = 'PASS' | 'POSITION' | 'SHOOT' | 'TACKLE';

export interface Duel {
  type: MatchActionType;
  pitchArea: string;
  side: MatchEventSide;
  initiator: Initiator;
  challenger: Initiator | null;
  receiver: Receiver | null;
  result: MatchResult;
  initiatorStats: RStats;
  challengerStats: RStats;
  origin: Origin;
  appliedPlayerOrder: null;
}

export interface Initiator {
  id: string;
  teamId: string;
  name: string;
  position: string;
  status: Status;
  skills: { [key: string]: number };
  playerOrder: PlayerOrder;
}

export enum Status {
  Active = 'ACTIVE',
}

export interface RStats {
  total: number;
  performance: number;
  skillPoints: number;
  carryover?: number;
  teamAssistance?: { [key: string]: number };
  assistance?: number;
}

export enum Origin {
  Default = 'DEFAULT',
}

export interface Receiver {
  id: string;
  teamId: string;
  name: string;
  position: string;
  status: Status;
  skills: Skills;
  playerOrder: PlayerOrder;
}

export interface Skills {
  BALL_CONTROL: number;
  DEFENSIVE_POSITIONING: number;
  PASSING: number;
  INTERCEPTING: number;
  TACKLING: number;
  AERIAL: number;
  SCORING: number;
  OFFENSIVE_POSITIONING: number;
  CONSTITUTION: number;
}

export enum MatchResult {
  Lose = 'LOSE',
  Win = 'WIN',
}

export enum MatchActionType {
  Pass = 'PASS',
  POSITION = 'POSITION',
  Tackle = 'TACKLE',
  Shoot = 'SHOOT',
}

export type MatchEventSide = 'MainEvent' | 'HomeTeamEvent' | 'AwayTeamEvent';

export type DuelResultRange = {
  min: number;
  max: number;
  label: string;
};

export const DENOMINATIONS_RANGES: DuelResultRange[] = [
  { min: 0, max: 14, label: 'Awful' },
  { min: 15, max: 29, label: 'Poor' },
  { min: 30, max: 39, label: 'Weak' },
  { min: 40, max: 49, label: 'Decent' },
  { min: 50, max: 59, label: 'Good' },
  { min: 60, max: 69, label: 'Excellent' },
  { min: 70, max: 79, label: 'Superb' },
  { min: 80, max: 89, label: 'Brilliant' },
  { min: 90, max: 99, label: 'Awesome' },
  { min: 100, max: 109, label: 'Masterful' },
  { min: 110, max: 120, label: 'Unbelievable' },
];

export const POSITIONAL_RANGES: DuelResultRange[] = [
  { min: -25, max: -21, label: 'In perfect position' },
  { min: -20, max: -16, label: 'Ready' },
  { min: -15, max: -11, label: 'Within reach' },
  { min: -10, max: -6, label: 'Near' },
  { min: -5, max: -1, label: 'Just about' },
  { min: 0, max: 0, label: 'There' },
  { min: 1, max: 5, label: 'Almost there' },
  { min: 6, max: 10, label: 'Close' },
  { min: 11, max: 15, label: 'In the vicinity' },
  { min: 16, max: 20, label: 'Far' },
  { min: 21, max: 25, label: 'Very far' },
];

export const ASSISTANCE_RANGES: DuelResultRange[] = [
  { min: -25, max: -21, label: 'Negligible' },
  { min: -20, max: -16, label: 'Minimal' },
  { min: -15, max: -11, label: 'Inadequate' },
  { min: -10, max: -6, label: 'Limited' },
  { min: -5, max: -1, label: 'Restricted' },
  { min: 0, max: 0, label: 'Basic' },
  { min: 1, max: 5, label: 'Adequate' },
  { min: 6, max: 10, label: 'Valuable' },
  { min: 11, max: 15, label: 'Exceptional' },
  { min: 16, max: 20, label: 'Outstanding' },
  { min: 21, max: 25, label: 'Extraordinary' },
];
