import { connectorAPI } from '@/libs/fetcher';
import useSWR from 'swr';

const API = '/team/';

const fetcher = (teamId: string | null, token: string | null) => {
  if (token === null || teamId === null) {
    return undefined;
  }
  return connectorAPI<any>(
    API + teamId + '/validate',
    'GET',
    undefined,
    undefined,
    token,
  );
};

const useTeamFormationValidationRepository = (
  teamId?: string,
  token?: string,
) => {
  const { data, error, isLoading, mutate } = useSWR<any>(
    token ? `${API}${teamId}/validate` : null,
    () => fetcher(teamId ? teamId : null, token ? token : null),
  );

  const refetch = () => {
    if (teamId && token) {
      mutate();
    }
  };

  return { data, isLoading, error, refetch };
};

export { useTeamFormationValidationRepository };
