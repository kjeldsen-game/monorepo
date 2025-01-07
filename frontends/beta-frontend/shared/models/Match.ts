export interface Match {
  id: string;
  home: MatchTeam;
  away: MatchTeam;
  dateTime: string;
  status: MatchStatus;
}

export enum MatchStatusEnum {
  PENDING = 'PENDING',
  ACCEPTED = 'ACCEPTED',
  REJECTED = 'REJECTED',
  SCHEDULED = 'SCHEDULED',
  PLAYED = 'PLAYED',
}
export type MatchStatus =
  | 'PENDING'
  | 'ACCEPTED'
  | 'REJECTED'
  | 'SCHEDULED'
  | 'PLAYED';

export interface MatchTeam {
  id: string;
  modifiers: Modifiers;
}

export interface Modifiers {
  tactic: string;
  horizontalPressure: string;
  verticalPressure: string;
}
