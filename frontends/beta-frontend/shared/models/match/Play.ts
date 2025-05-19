import { PitchArea } from './PitchArea';
import { Player } from '../player/Player';
import { BallState } from './BallState';
import { PlayerOrder } from '../player/PlayerOrder';

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
