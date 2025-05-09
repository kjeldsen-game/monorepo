import { connectorAPI } from '@/libs/fetcher';
import useSWR from 'swr';

const API = '/match/';

const fetcher = (
  teamId: string | null,
  token: string | null,
  matchId: string | null,
  selfChallenge: boolean = false,
) => {
  if (token === null || teamId === null || matchId === null) {
    return undefined;
  }
  return connectorAPI<any>(
    `${API}${matchId}/teams/${teamId}/validate?selfChallenge=${selfChallenge}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};

const useMatchTeamFormationValidationRepository = (
  teamId?: string,
  matchId?: string,
  token?: string,
  selfChallenge: boolean = false,
) => {
  console.log(selfChallenge);
  const { data, error, isLoading, mutate } = useSWR<any>(
    token ? `${API}${matchId}/teams/${teamId}/validate` : null,
    () =>
      fetcher(
        teamId ? teamId : null,
        token ? token : null,
        matchId ? matchId : null,
        selfChallenge,
      ),
  );

  const refetch = () => {
    if (teamId && token && matchId) {
      mutate();
    }
  };

  return { data, isLoading, error, refetch };
};

export { useMatchTeamFormationValidationRepository };
