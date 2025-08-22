import { Player } from '@/shared/models/player/Player';
import { PlayerPosition } from '@/shared/models/player/PlayerPosition';

export const POSITION_LIMITS: Partial<Record<PlayerPosition, number>> = {
  [PlayerPosition.GOALKEEPER]: 1,
  [PlayerPosition.LEFT_BACK]: 1,
  [PlayerPosition.CENTRE_BACK]: 3,
  [PlayerPosition.RIGHT_BACK]: 1,
  [PlayerPosition.DEFENSIVE_MIDFIELDER]: 2,
  [PlayerPosition.OFFENSIVE_MIDFIELDER]: 2,
  [PlayerPosition.CENTRE_MIDFIELDER]: 3,
  [PlayerPosition.LEFT_MIDFIELDER]: 1,
  [PlayerPosition.RIGHT_MIDFIELDER]: 1,
  [PlayerPosition.LEFT_WINGBACK]: 1,
  [PlayerPosition.RIGHT_WINGBACK]: 1,
  [PlayerPosition.LEFT_WINGER]: 1,
  [PlayerPosition.RIGHT_WINGER]: 1,
  [PlayerPosition.FORWARD]: 3,
};

type PositionLimits = {
  GOALKEEPER: 1;
  LEFT_BACK: 1;
  CENTRE_BACK: 3;
  RIGHT_BACK: 1;
  DEFENSIVE_MIDFIELDER: 2;
  OFFENSIVE_MIDFIELDER: 2;
  CENTRE_MIDFIELDER: 3;
  LEFT_MIDFIELDER: 1;
  RIGHT_MIDFIELDER: 1;
  LEFT_WINGBACK: 1;
  RIGHT_WINGBACK: 1;
  LEFT_WINGER: 1;
  RIGHT_WINGER: 1;
  FORWARD: 3;
};

type FixedArray<T, L extends number, R extends T[] = []> = R['length'] extends L
  ? R
  : FixedArray<T, L, [...R, T]>;

export type Lineup = {
  [P in keyof PositionLimits]: PositionLimits[P] extends 1
    ? Player | undefined
    : FixedArray<Player | undefined, PositionLimits[P]>;
};
