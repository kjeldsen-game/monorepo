import { connectorAPI } from '@/libs/fetcher';
import { TeamResponse } from '../types/Responses';

export const TEAMS_API = '/team';

export const getTeams = async (
  token: string | null,
): Promise<TeamResponse[]> => {
  return connectorAPI<undefined>(
    `${TEAMS_API}?page=${0}&size=${99}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};

export const getTeam = (
  teamId?: string | null,
  token?: string | null,
): Promise<TeamResponse> => {
  if (token === null || teamId === null) {
    return undefined;
  }
  return connectorAPI<undefined>(
    TEAMS_API + '/' + teamId,
    'GET',
    undefined,
    undefined,
    token,
  );
};
