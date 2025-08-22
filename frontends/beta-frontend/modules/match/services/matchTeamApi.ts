import { connectorAPI } from '@/libs/fetcher';
import { MATCH_API } from './constants';
import { Player } from '@/shared/models/player/Player';
import { TeamModifiers } from 'modules/player/types/TeamModifiers';
import { TeamPlayerPatchRequest } from 'modules/player/types/Requests';

export const getMatchTeamByTeamIdAndMatchId = (
  matchId?: string | undefined | string[],
  teamId?: string | undefined | string[],
  token?: string,
) => {
  if (token === undefined || matchId === undefined || teamId === undefined) {
    return undefined;
  }
  return connectorAPI<any>(
    `${MATCH_API}/${matchId}/team/${teamId}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};

export const updateMatchTeamByTeamIdAndMatchId = (
  matchId?: string | undefined | string[],
  teamId?: string | undefined | string[],
  token?: string,
  request?: TeamPlayerPatchRequest,
): Promise<any> => {
  if (token === null || matchId === null || teamId === null) {
    return undefined;
  }
  return connectorAPI<TeamPlayerPatchRequest>(
    `${MATCH_API}/${matchId}/team/${teamId}`,
    'PUT',
    request,
    undefined,
    token,
  );
};
