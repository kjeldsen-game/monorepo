import { theme } from '@/libs/material/theme';
import { PlayerSkillRelevance } from '@/shared/models/player/Player';

export const getSkillRelevanceGradient = (relevance: PlayerSkillRelevance) => {
  switch (relevance) {
    case PlayerSkillRelevance.CORE:
      return 'linear-gradient(90deg, #62D160 0%, #429F40 100%)';
      `linear-gradient(90deg, ${theme.palette.secondary.main} 0%, #C53064 100%)`;
    case PlayerSkillRelevance.SECONDARY:
      return 'linear-gradient(90deg, #F68B29 0%, #C56D1C 100%)';
      `linear-gradient(90deg, ${theme.palette.tertiary.main} 0%, #D1D5DB 100%)`;
    case PlayerSkillRelevance.RESIDUAL:
      return 'linear-gradient(90deg, #29B6F6 0%, #1C97CE 100%)';
      `linear-gradient(90deg, ${theme.palette.quaternary.main} 0%, #333A42 100%)`;
    default:
      return 'linear-gradient(90deg, #FFFFFF 0%, #E0E0E0 100%)';
  }
};

export const getSkillRelevanceColors = (relevance: PlayerSkillRelevance) => {
  switch (relevance) {
    case PlayerSkillRelevance.CORE:
      return { start: '#62D160', end: '#429F40' };
    case PlayerSkillRelevance.SECONDARY:
      return { start: '#F68B29', end: '#C56D1C' };
    case PlayerSkillRelevance.RESIDUAL:
      return { start: '#29B6F6', end: '#1C97CE' };
    default:
      return { start: '#FFFFFF', end: '#E0E0E0' };
  }
};
