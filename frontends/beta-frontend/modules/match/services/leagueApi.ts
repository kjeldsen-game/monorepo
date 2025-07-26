import { connectorAPI } from '@/libs/fetcher';
import { LeagueResponse } from '../types/LeagueResponses';

export const LEAGUE_API = '/league';

export const getLeague = (
  leagueId: string | null,
  token: string | null,
): Promise<LeagueResponse> => {
  if (token === null || leagueId === null) {
    return undefined;
  }
  return connectorAPI<any>(
    LEAGUE_API + '/' + leagueId,
    'GET',
    undefined,
    undefined,
    token,
  );
};
