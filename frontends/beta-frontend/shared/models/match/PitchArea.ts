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
  OUT_OF_BOUNDS = 'OUT_OF_BOUNDS',
}

export const PITCH_AREA_SHORTCUTS: Record<PITCH_AREAS_ENUM, string> = {
  [PITCH_AREAS_ENUM.LEFT_FORWARD]: 'LF',
  [PITCH_AREAS_ENUM.CENTRE_FORWARD]: 'CF',
  [PITCH_AREAS_ENUM.RIGHT_FORWARD]: 'RF',
  [PITCH_AREAS_ENUM.LEFT_MIDFIELD]: 'LM',
  [PITCH_AREAS_ENUM.CENTRE_MIDFIELD]: 'CM',
  [PITCH_AREAS_ENUM.RIGHT_MIDFIELD]: 'RM',
  [PITCH_AREAS_ENUM.LEFT_BACK]: 'LB',
  [PITCH_AREAS_ENUM.CENTRE_BACK]: 'CB',
  [PITCH_AREAS_ENUM.RIGHT_BACK]: 'RB',
  [PITCH_AREAS_ENUM.OUT_OF_BOUNDS]: 'OB',
};
