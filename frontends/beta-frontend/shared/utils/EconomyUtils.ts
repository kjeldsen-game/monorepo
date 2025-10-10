import { IncomeMode, IncomePeriodicity } from '../models/player/Economy';

export const getBackgroundColor = (name: string) => {
  switch (name) {
    case 'Total Income':
      return {
        backgroundColor: '#A4BC100D',
      };
    case 'Total Outcome':
      return {
        backgroundColor: '#C51A1A0D',
      };
    case 'Total Balance':
      return {
        backgroundColor: '#F5F5F5',
      };
    default:
      return {
        backgroundColor: 'transparent',
      };
  }
};

export const isNegative = (input: number) => {
  return input < 0;
};

export const filterSponsorsByPeriodicity = (
  sponsors: any[],
  periodicity: IncomePeriodicity,
): IncomeMode | null => {
  if (!sponsors) return null;
  const sponsor = sponsors.find(
    (sponsor) => sponsor.periodicity === periodicity.toUpperCase(),
  );
  return sponsor ? sponsor.mode : null;
};
