export interface LeagueResponse {
  id: string;
  name: string;
  tier: number;
  startedAt: string;
  season: number;
  scheduledMatches: boolean;
  teams: Record<string, LeagueStats>;
}

export interface LeagueStats {
  name: string;
  draws: number;
  gamesPlayed: number;
  goalsReceived: number;
  goalsScored: number;
  losses: number;
  points: number;
  position: number;
  wins: number;
}
