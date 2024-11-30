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
  const { data, isLoading, error } = useSWR<MatchReport | undefined>(
    API + matchId,
    () => fetcher(matchId ? matchId : null, token ? token : null),
  );

  return { data, isLoading, error };
};

export { useMatchReportRepository };
