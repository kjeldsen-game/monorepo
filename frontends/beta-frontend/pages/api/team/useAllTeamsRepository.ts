import { connectorAPI } from '@/libs/fetcher';
import useSWR from 'swr';
import { Team } from '@/shared/models/Team';

const API = '/team';

const fetcher = (page: number, size: number, token?: string | null) => {
  if (token === null) {
    return [];
  }
  return connectorAPI<any>(
    API + `?page=${page}&size=${size}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};

const useAllTeamsRepository = (page: number, size: number, token: string) => {
  const { data: allTeams } = useSWR<Team[]>([API, page, size], () =>
    fetcher(page, size, token ? token : null),
  );
  console.log(allTeams);
  return { allTeams };
};
export { useAllTeamsRepository };
