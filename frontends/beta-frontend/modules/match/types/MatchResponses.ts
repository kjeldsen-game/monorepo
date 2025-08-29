import { BallState } from '@/shared/models/MatchReport';
import { Player } from '@/shared/models/player/Player';
import { PlayerOrder } from '@/shared/models/player/PlayerOrder';
import { PitchArea } from 'modules/player/types/Player';
import { TeamModifiers } from 'modules/player/types/TeamModifiers';

export interface MatchResponse {
  id: string;
  away: MatchTeam;
  home: MatchTeam;
  dateTime: string;
  matchReport: MatchReport;
  status: MatchStatus;
}

export interface MatchTeam {
  id: string;
  name: string;
  role: string;
  players: Player[];
  bench: Player[];
  specificLineup: boolean;
  tactic: string;
  verticalPressure: string;
  horizontalPressure: string;
  modifiers: TeamModifiers;
}

export enum MatchStatus {
  PENDING = 'PENDING',
  ACCEPTED = 'ACCEPTED',
  REJECTED = 'REJECTED',
  SCHEDULED = 'SCHEDULED',
  PLAYED = 'PLAYED',
}

export interface MatchReport {
  home: MatchTeam;
  away: MatchTeam;
  plays: Play[];
  homeStats: MatchStats;
  awayStats: MatchStats;
}

export interface Stats {
  goals: number;
  missed: number;
  shots: number;
  passes: number;
  missedPasses: number;
  tackles: number;
  saved: number;
}

export interface MatchStats extends Stats {
  playerStats: { [key: string]: Stats };
}

export type MatchEventSide = 'MainEvent' | 'HomeTeamEvent' | 'AwayTeamEvent';

export type DuelResultRange = {
  min: number;
  max: number;
  label: string;
  color: string;
};

export const DENOMINATIONS_RANGES: DuelResultRange[] = [
  { min: 0, max: 14, label: 'Awful', color: '#8B0000' },
  { min: 15, max: 29, label: 'Poor', color: '#B22222' },
  { min: 30, max: 39, label: 'Weak', color: '#D2691E' },
  { min: 40, max: 49, label: 'Decent', color: '#FF8C00' },
  { min: 50, max: 59, label: 'Good', color: '#FFA500' },
  { min: 60, max: 69, label: 'Excellent', color: '#FFB347' },
  { min: 70, max: 79, label: 'Superb', color: '#228B22' },
  { min: 80, max: 89, label: 'Brilliant', color: '#006400' },
  { min: 90, max: 99, label: 'Awesome', color: '#004d00' },
  { min: 100, max: 109, label: 'Masterful', color: '#003300' },
  { min: 110, max: 120, label: 'Unbelievable', color: '#001a00' },
];

export const POSITIONAL_RANGES: DuelResultRange[] = [
  { min: -25, max: -21, label: 'In perfect position', color: '#004d00' },
  { min: -20, max: -16, label: 'Ready', color: '#006400' },
  { min: -15, max: -11, label: 'Within reach', color: '#228B22' },
  { min: -10, max: -6, label: 'Near', color: '#FFA500' },
  { min: -5, max: -1, label: 'Just about', color: '#FF8C00' },
  { min: 0, max: 0, label: 'There', color: '#FF8C00' },
  { min: 1, max: 5, label: 'Almost there', color: '#D2691E' },
  { min: 6, max: 10, label: 'Close', color: '#B22222' },
  { min: 11, max: 15, label: 'In the vicinity', color: '#8B0000' },
  { min: 16, max: 20, label: 'Far', color: '#8B0000' },
  { min: 21, max: 25, label: 'Very far', color: '#640000' },
];

export const ASSISTANCE_RANGES: DuelResultRange[] = [
  { min: -25, max: -21, label: 'Negligible', color: '#8B0000' },
  { min: -20, max: -16, label: 'Minimal', color: '#B22222' },
  { min: -15, max: -11, label: 'Inadequate', color: '#D2691E' },
  { min: -10, max: -6, label: 'Limited', color: '#FF8C00' },
  { min: -5, max: -1, label: 'Restricted', color: '#FFA500' },
  { min: 0, max: 0, label: 'Basic', color: '#FFA500' },
  { min: 1, max: 5, label: 'Adequate', color: '#FFB347' },
  { min: 6, max: 10, label: 'Valuable', color: '#228B22' },
  { min: 11, max: 15, label: 'Exceptional', color: '#006400' },
  { min: 16, max: 20, label: 'Outstanding', color: '#004d00' },
  { min: 21, max: 25, label: 'Extraordinary', color: '#003300' },
];

export interface Play {
  homeScore: number;
  awayScore: number;
  action: Action;
  duel: Duel;
  clock: number;
  ballState: BallState;
  chainActionSequence: ChainActionSequence;
}

export interface Duel {
  type: DuelType;
  pitchArea: PitchArea;
  initiator: Player;
  challenger?: Player;
  receiver?: Player;
  result: DuelResult;
  initiatorStats: DuelStats;
  challengerStats: DuelStats;
  origin: DuelOrigin;
  appliedPlayerOrder: PlayerOrder;
  duelDisruption: DuelDisruption;
}

export enum DuelOrigin {
  DEFAULT = 'DEFAULT',
  PLAYER_ORDER = 'PLAYER_ORDER',
}

export interface DuelDisruption {
  type: DuelDisruptionType;
  destinationPitchArea?: PitchArea;
  challenger?: Player;
  receiver?: Player;
  difference: number;
  random: number;
}

export enum DuelDisruptionType {
  NONE = 'NONE',
  GOALKEEPER_INTERCEPTION = 'GOALKEEPER_INTERCEPTION',
  MISSED_PASS = 'MISSED_PASS',
  MISSED_SHOT = 'MISSED_SHOT',
}

export interface DuelStats {
  total: number;
  skillPoints: number;
  carryover?: number;
  performance: Performance;
  assistance?: Assistance;
  chainActionSequence?: {
    [key in ChainActionSequence]?: number;
  };
}

export interface Performance {
  total: number;
  random: number;
  previousTotalImpact: number;
}

export interface Assistance {
  total: number;
  totalModifiers: number;
  adjusted: number;
  teamAssistance?: Record<string, number>;
  modifiers?: Record<ChainActionSequence, number>;
}

export enum DuelResult {
  WIN = 'WIN',
  LOSE = 'LOSE',
}

export enum DuelType {
  PASSING_LOW = 'PASSING_LOW',
  PASSING_HIGH = 'PASSING_HIGH',
  DRIBBLE = 'DRIBBLE',
  POSITIONAL = 'POSITIONAL',
  BALL_CONTROL = 'BALL_CONTROL',
  LOW_SHOT = 'LOW_SHOT',
  ONE_TO_ONE_SHOT = 'ONE_TO_ONE',
  HEADER_SHOT = 'HEADER_SHOT',
  LONG_SHOT = 'LONG_SHOT',
  THROW_IN = 'THROW_IN',
}

export enum Action {
  PASS = 'PASS',
  POSITION = 'POSITION',
  SHOOT = 'SHOOT',
  TACKLE = 'TACKLE',
  DRIBBLE = 'DRIBBLE',
}

export enum ChainActionSequence {
  NONE = 'NONE',
  WALL_PASS = 'WALL_PASS',
  COUNTER_ATTACK = 'COUNTER_ATTACK',
}
