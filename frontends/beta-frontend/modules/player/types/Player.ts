export enum PitchArea {
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

export const PitchAreaShortcuts: Record<PitchArea, string> = {
  [PitchArea.LEFT_FORWARD]: 'LF',
  [PitchArea.CENTRE_FORWARD]: 'CF',
  [PitchArea.RIGHT_FORWARD]: 'RF',
  [PitchArea.LEFT_MIDFIELD]: 'LM',
  [PitchArea.CENTRE_MIDFIELD]: 'CM',
  [PitchArea.RIGHT_MIDFIELD]: 'RM',
  [PitchArea.LEFT_BACK]: 'LB',
  [PitchArea.CENTRE_BACK]: 'CB',
  [PitchArea.RIGHT_BACK]: 'RB',
  [PitchArea.OUT_OF_BOUNDS]: 'OOB',
};
