import useSWR from 'swr';
import { MatchReport } from '@/shared/models/MatchReport';
import { connectorAPI } from '@/libs/fetcher';

const API = '/match';

const fetcher = (matchId: string | null | string[], token: string | null) => {
  if (token === null || matchId === null) return undefined;
  return connectorAPI<any>(
    `${API}/${matchId}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};

const useMatchReportRepository = (
  matchId?: string | undefined | string[],
  token?: string,
) => {
  console.log('refetch thisss');
  const { data, isLoading, error, mutate } = useSWR<MatchReport | undefined>(
    token ? API + 'report/' + matchId : null,
    () => fetcher(matchId ? matchId : null, token ? token : null),
  );
  const refetch = () => {
    mutate();
  };

  return { data, isLoading, error, refetch };
};

export { useMatchReportRepository };
