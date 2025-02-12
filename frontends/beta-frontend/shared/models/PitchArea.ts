export type PitchArea =
  | 'LEFT_FORWARD'
  | 'CENTRE_FORWARD'
  | 'RIGHT_FORWARD'
  | 'LEFT_MIDFIELD'
  | 'CENTRE_MIDFIELD'
  | 'RIGHT_MIDFIELD'
  | 'LEFT_BACK'
  | 'CENTRE_BACK'
  | 'RIGHT_BACK';

export const PITCH_AREAS: PitchArea[] = [
  'LEFT_FORWARD',
  'CENTRE_FORWARD',
  'RIGHT_FORWARD',
  'LEFT_MIDFIELD',
  'CENTRE_MIDFIELD',
  'RIGHT_MIDFIELD',
  'LEFT_BACK',
  'CENTRE_BACK',
  'RIGHT_BACK',
];
export enum PITCH_AREAS_ENUM {
  LEFT_FORWARD = 'LEFT_FORWARD',
  CENTRE_FORWARD = 'CENTRE_FORWARD',
  RIGHT_FORWARD = 'RIGHT_FORWARD',
  LEFT_MIDFIELD = 'LEFT_MIDFIELD',
  CENTRE_MIDFIELD = 'CENTRE_MIDFIELD',
  RIGHT_MIDFIELD = 'RIGHT_MIDFIELD',
  LEFT_BACK = 'LEFT_BACK',
  CENTRE_BACK = 'CENTRE_BACK',
  RIGHT_BACK = 'RIGHT_BACK',
}
