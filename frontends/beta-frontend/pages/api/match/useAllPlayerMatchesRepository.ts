import { connectorAPI } from '@/libs/fetcher';
import useSWR, { mutate } from 'swr';
import { Match } from '@/shared/models/Match';

const API = '/match';

const fetcher = (
  page: number,
  size: number,
  teamId: string | null,
  token: string | null,
) => {
  if (token === null || teamId === null) return [];
  return connectorAPI<any>(
    `${API}?page=${page}&size=${size}&teamId=${teamId}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};

const useAllPlayerMatchesRepository = (
  page: number = 0,
  size: number = 99,
  teamId?: string,
  token?: string,
) => {
  const { data: allMatches, mutate } = useSWR<Match[]>([API, teamId], () =>
    fetcher(page, size, teamId ? teamId : null, token ? token : null),
  );

  const refetch = () => {
    mutate();
  };

  const pastMatches = allMatches?.filter(
    (match) => new Date(match.dateTime).getTime() < new Date().getTime(),
  );
  const incomingMatches = allMatches?.filter(
    (match) => new Date(match.dateTime).getTime() > new Date().getTime(),
  );
  const acceptedMatches = allMatches?.filter(
    (match) => match.status === 'ACCEPTED',
  );

  return { allMatches, pastMatches, incomingMatches, acceptedMatches, refetch };
};
export { useAllPlayerMatchesRepository };
