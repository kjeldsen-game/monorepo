import { PlayerSkill } from '@/shared/models/player/PlayerSkill';
import { renderSkillNames } from '../TrainingUtils';

describe('TrainingUtils', () => {
  it('should return empty string when skills is empty', () => {
    expect(renderSkillNames([])).toBe('');
  });

  it('should return single skill shortcut', () => {
    expect(renderSkillNames([PlayerSkill.SCORING])).toBe('SC');
  });

  it('should return skills shortcuts devided by /', () => {
    expect(renderSkillNames([PlayerSkill.SCORING, PlayerSkill.AERIAL])).toBe(
      'SC / AE',
    );
  });
});
