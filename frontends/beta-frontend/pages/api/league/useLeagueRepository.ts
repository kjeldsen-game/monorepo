import { connectorAPI } from '@/libs/fetcher';
import useSWR from 'swr';

const API = '/league';

const fetcher = (leagueId: string | null, token: string | null) => {
  if (token === null || leagueId === null) {
    return undefined;
  }
  return connectorAPI<any>(
    API + '/' + leagueId,
    'GET',
    undefined,
    undefined,
    token,
  );
};

const useLeagueRepository = (leagueId?: string, token?: string) => {
  const { data, error, isLoading, mutate } = useSWR<any>(
    leagueId ? API + leagueId : null,
    () => fetcher(leagueId ? leagueId : null, token ? token : null),
  );

  return { data, isLoading, error };
};

export { useLeagueRepository };
