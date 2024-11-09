export enum IncomeMode {
  CONSERVATIVE = 'Conservatire',
  MODERATE = 'Moderate',
  AGGRESSIVE = 'Aggressive',
}

export enum IncomePeriodicity {
  WEEKLY = 'Weekly',
  ANNUAL = 'Annual',
}

export interface Sponsor {
  periodicity: IncomePeriodicity;
  mode: IncomeMode | null;
}

export const SPONSORS_OFFERS = {
  [IncomePeriodicity.ANNUAL]: {
    [IncomeMode.CONSERVATIVE]: {
      base: 1000000,
      bonus: 100000,
    },
    [IncomeMode.MODERATE]: {
      base: 750000,
      bonus: 200000,
    },
    [IncomeMode.AGGRESSIVE]: {
      base: 500000,
      bonus: 300000,
    },
  },
  [IncomePeriodicity.WEEKLY]: {
    [IncomeMode.CONSERVATIVE]: {
      base: 100000,
      bonus: 0,
    },
    [IncomeMode.MODERATE]: {
      base: 50000,
      bonus: 100000,
    },
    [IncomeMode.AGGRESSIVE]: {
      base: 0,
      bonus: 200000,
    },
  },
};

export interface Pricing {
  pricingType: string;
  price: number;
}
