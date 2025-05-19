import { Player } from '../player/Player';
import { PitchArea } from './PitchArea';

export interface BallState {
  height: BallHeight;
  player: Player;
  pitchArea: PitchArea;
}

export enum BallHeight {
  GROUND = 'GROUND',
  LOW = 'LOW',
  HIGH = 'HIGH',
}
