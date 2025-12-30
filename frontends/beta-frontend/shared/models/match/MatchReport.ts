import { Play } from './Play';
import { MatchTeam } from './Match';

export interface MatchReport {
  home: MatchTeam;
  away: MatchTeam;
  plays: Play[];
  homeStats: MatchStats;
  awayStats: MatchStats;
}

export interface Stats {
  goals: number;
  missed: number;
  shots: number;
  passes: number;
  missedPasses: number;
  tackles: number;
  saved: number;
}

export interface MatchStats extends Stats {
  playerStats: { [key: string]: Stats };
}

export type MatchEventSide = 'MainEvent' | 'HomeTeamEvent' | 'AwayTeamEvent';

export type DuelResultRange = {
  min: number;
  max: number;
  label: string;
  color: string;
};

export const DENOMINATIONS_RANGES: DuelResultRange[] = [
  { min: 0, max: 14, label: 'Awful', color: '#8B0000' },
  { min: 15, max: 29, label: 'Poor', color: '#B22222' },
  { min: 30, max: 39, label: 'Weak', color: '#D2691E' },
  { min: 40, max: 49, label: 'Decent', color: '#FF8C00' },
  { min: 50, max: 59, label: 'Good', color: '#FFA500' },
  { min: 60, max: 69, label: 'Excellent', color: '#FFB347' },
  { min: 70, max: 79, label: 'Superb', color: '#228B22' },
  { min: 80, max: 89, label: 'Brilliant', color: '#006400' },
  { min: 90, max: 99, label: 'Awesome', color: '#004d00' },
  { min: 100, max: 109, label: 'Masterful', color: '#003300' },
  { min: 110, max: 120, label: 'Unbelievable', color: '#001a00' },
];

export const POSITIONAL_RANGES: DuelResultRange[] = [
  { min: -25, max: -21, label: 'In perfect position', color: '#004d00' },
  { min: -20, max: -16, label: 'Ready', color: '#006400' },
  { min: -15, max: -11, label: 'Within reach', color: '#228B22' },
  { min: -10, max: -6, label: 'Near', color: '#FFA500' },
  { min: -5, max: -1, label: 'Just about', color: '#FF8C00' },
  { min: 0, max: 0, label: 'There', color: '#FF8C00' },
  { min: 1, max: 5, label: 'Almost there', color: '#D2691E' },
  { min: 6, max: 10, label: 'Close', color: '#B22222' },
  { min: 11, max: 15, label: 'In the vicinity', color: '#8B0000' },
  { min: 16, max: 20, label: 'Far', color: '#8B0000' },
  { min: 21, max: 25, label: 'Very far', color: '#640000' },
];

export const ASSISTANCE_RANGES: DuelResultRange[] = [
  { min: -25, max: -21, label: 'Negligible', color: '#8B0000' },
  { min: -20, max: -16, label: 'Minimal', color: '#B22222' },
  { min: -15, max: -11, label: 'Inadequate', color: '#D2691E' },
  { min: -10, max: -6, label: 'Limited', color: '#FF8C00' },
  { min: -5, max: -1, label: 'Restricted', color: '#FFA500' },
  { min: 0, max: 0, label: 'Basic', color: '#FFA500' },
  { min: 1, max: 5, label: 'Adequate', color: '#FFB347' },
  { min: 6, max: 10, label: 'Valuable', color: '#228B22' },
  { min: 11, max: 15, label: 'Exceptional', color: '#006400' },
  { min: 16, max: 20, label: 'Outstanding', color: '#004d00' },
  { min: 21, max: 25, label: 'Extraordinary', color: '#003300' },
];

export const GOALKEEPING_RANGES: DuelResultRange[] = [
  { min: -25, max: -21, label: 'Totally in the wrong position', color: '#8B0000' },
  { min: -20, max: -16, label: 'Significantly out of position', color: '#B22222' },
  { min: -15, max: -11, label: 'Near but ineffective', color: '#D2691E' },
  { min: -10, max: -6,  label: 'Close but exposed', color: '#FF8C00' },
  { min: -5,  max: -1,  label: 'Almost there', color: '#FFA500' },
  { min: 0,   max: 0,   label: 'In position', color: '#FFA500' },
  { min: 1,   max: 5,   label: 'Just about ready', color: '#FFB347' },
  { min: 6,   max: 10,  label: 'Solid position', color: '#228B22' },
  { min: 11,  max: 15,  label: 'Secure', color: '#006400' },
  { min: 16,  max: 20,  label: 'Ready', color: '#004d00' },
  { min: 21,  max: 25,  label: 'In perfect position', color: '#003300' },
];
