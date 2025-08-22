import { Transaction } from "../types/Economy";

export const mockTransactions: Transaction[] = [
  { thisWeekAmount: 0, thisSeasonAmount: 0, context: 'SPONSOR' },
  { thisWeekAmount: 0, thisSeasonAmount: 0, context: 'PLAYER_SALE' },
  { thisWeekAmount: -400, thisSeasonAmount: -400, context: 'PLAYER_PURCHASE' },
  { thisWeekAmount: 0, thisSeasonAmount: 0, context: 'ATTENDANCE' },
  { thisWeekAmount: 0, thisSeasonAmount: 0, context: 'MERCHANDISE' },
  { thisWeekAmount: -15411250, thisSeasonAmount: -15411250, context: 'PLAYER_WAGE' },
  { thisWeekAmount: 0, thisSeasonAmount: 0, context: 'BUILDING_UPGRADE' },
  { thisWeekAmount: 0, thisSeasonAmount: 0, context: 'BUILDING_MAINTENANCE' },
  { thisWeekAmount: 0, thisSeasonAmount: 0, context: 'RESTAURANT' },
  { thisWeekAmount: 283140, thisSeasonAmount: 283140, context: 'BILLBOARDS' },
  { thisWeekAmount: 283140, thisSeasonAmount: 283140, context: 'Total Income' },
  { thisWeekAmount: -15411650, thisSeasonAmount: -15411650, context: 'Total Outcome' },
  { thisWeekAmount: -15128510, thisSeasonAmount: -15128510, context: 'Total Balance' },
];
