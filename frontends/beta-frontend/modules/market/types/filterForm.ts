export interface MarketFilterForm {
  playerOffer: MinMaxRange;
  playerAge: MinMaxRange;
  position: string;
  skillRanges: SkillRanges;
}

export type MinMaxRange = {
  min: string;
  max: string;
  potentialMin?: string;
  potentialMax?: string;
};

export type SkillKey = 'SC' | 'OP' | 'BC' | 'PA' | 'AE' | 'CO' | 'TA' | 'DP';

export type SkillRanges = {
  [key in SkillKey]: MinMaxRange;
};
