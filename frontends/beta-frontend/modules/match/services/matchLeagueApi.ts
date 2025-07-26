import { connectorAPI } from '@/libs/fetcher';

export const MATCH_LEAGUE_API = '/match';

export const getLeagueMatches = (
  page: number,
  size: number,
  leagueId: string | null,
  token: string | null,
) => {
  if (token === null || leagueId === null) return [];
  return connectorAPI<any>(
    `${MATCH_LEAGUE_API}?page=${page}&size=${size}&leagueId=${leagueId}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};
