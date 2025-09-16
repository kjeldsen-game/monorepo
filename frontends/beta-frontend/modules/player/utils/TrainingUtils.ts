import {
  PlayerSkill,
  PlayerSkillToShortcut,
} from '@/shared/models/player/PlayerSkill';
import { TrainingType } from '../types/TrainingResponses';

export const renderSkillNames = (skills: PlayerSkill[]): string => {
  if (!skills || skills.length === 0) {
    return '';
  }

  return skills
    .slice(0, 2)
    .map((skill) => PlayerSkillToShortcut[skill] || skill)
    .join(' / ');
};

export const getArrowColorTrainingDataGrid = (trainingType: TrainingType) => {
  switch (trainingType) {
    case TrainingType.PLAYER_TRAINING:
      return '#4CAF50';
    case TrainingType.DECLINE_TRAINING:
      return '#F44336';
    case TrainingType.POTENTIAL_RISE:
      return '#2196F3';
    default:
      return '#A4BC10';
  }
};
