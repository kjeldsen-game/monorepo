import { connectorAPI } from '@/libs/fetcher';
import useSWR, { mutate } from 'swr';
import { Match } from '@/shared/models/match/Match';

const API = '/match';

const fetcher = (
  page: number,
  size: number,
  leagueId: string | null,
  token: string | null,
) => {
  if (token === null || leagueId === null) return [];
  return connectorAPI<any>(
    `${API}?page=${page}&size=${size}&leagueId=${leagueId}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};

const useLeagueMatchesRepository = (
  leagueId?: string,
  token?: string,
  page: number = 0,
  size: number = 99,
) => {
  const { data, error, isLoading } = useSWR<any>(
    leagueId ? API + leagueId : null,
    () => fetcher(page, size, leagueId ? leagueId : null, token ? token : null),
  );

  return { data, isLoading, error };
};
export { useLeagueMatchesRepository };
