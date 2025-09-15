import { connectorAPI } from '@/libs/fetcher';
import useSWR from 'swr';

const API = '/training/';

const fetcher = (teamId: string | null, token: string | null) => {
  if (token === null || teamId === null) {
    return undefined;
  }
  return connectorAPI<any>(API + teamId, 'GET', undefined, undefined, token);
};

const useTrainingRepository = (teamId?: string, token?: string) => {
  const { data, error, isLoading, mutate } = useSWR<any>(
    token ? API : null,
    () => fetcher(teamId ? teamId : null, token ? token : null),
  );

  return { data, isLoading, error };
};

export { useTrainingRepository };
