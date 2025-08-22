import { PitchArea } from 'modules/player/types/Player';

export const PITCH_AREA_SHORTCUTS: Record<PitchArea, string> = {
  [PitchArea.LEFT_FORWARD]: 'LF',
  [PitchArea.CENTRE_FORWARD]: 'CF',
  [PitchArea.RIGHT_FORWARD]: 'RF',
  [PitchArea.LEFT_MIDFIELD]: 'LM',
  [PitchArea.CENTRE_MIDFIELD]: 'CM',
  [PitchArea.RIGHT_MIDFIELD]: 'RM',
  [PitchArea.LEFT_BACK]: 'LB',
  [PitchArea.CENTRE_BACK]: 'CB',
  [PitchArea.RIGHT_BACK]: 'RB',
  [PitchArea.OUT_OF_BOUNDS]: 'OB',
};
