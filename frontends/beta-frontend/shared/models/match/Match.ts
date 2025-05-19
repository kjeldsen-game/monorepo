import { MatchReport } from './MatchReport';
import { Player } from '../player/Player';
import { TeamModifiers } from '../player/TeamModifiers';

export interface Match {
  id: string;
  leagueId: string;
  home: MatchTeam;
  away: MatchTeam;
  dateTime: string;
  status: MatchStatus;
  report: MatchReport;
}

export enum MatchStatus {
  PENDING = 'PENDING',
  ACCEPTED = 'ACCEPTED',
  REJECTED = 'REJECTED',
  SCHEDULED = 'SCHEDULED',
  PLAYED = 'PLAYED',
}

export interface MatchTeam {
  id: string;
  name: string;
  role: string;
  players: Player[];
  bench: Player[];
  specificLineup: boolean;
  tactic: string;
  verticalPressure: string;
  horizontalPressure: string;
  modifiers: TeamModifiers;
}
