import { connectorAPI } from '@/libs/fetcher';
import { LineupValidationResponse, TeamResponse } from '../types/Responses';
import { Player } from '@/shared/models/player/Player';
import { TeamModifiers } from '@/shared/models/player/TeamModifiers';
import { PlayerRequest, TeamPlayerPatchRequest } from '../types/Requests';
import { DefaultResponse } from '@/shared/models/Responses';

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

export const getLineupValidation = (
  teamId: string | null,
  token: string | null,
): Promise<LineupValidationResponse> => {
  if (token === null || teamId === null) {
    return undefined;
  }
  return connectorAPI<any>(
    TEAMS_API + '/' + teamId + '/validate',
    'GET',
    undefined,
    undefined,
    token,
  );
};

export const updateLineup = (
  teamId: string,
  token: string | null,
  oldPlayers: Player[],
  newPlayers: Player[],
  teamModifiers: TeamModifiers,
): Promise<DefaultResponse> => {
  const request: TeamPlayerPatchRequest = {
    players: oldPlayers.map((player) => {
      const updatedPlayer = newPlayers.find(
        (playerValue) => playerValue.id === player.id,
      );
      if (updatedPlayer) {
        return buildPlayerRequest(updatedPlayer);
      }
      return buildPlayerRequest(player);
    }),
    teamModifiers,
  };
  return connectorAPI<TeamPlayerPatchRequest>(
    TEAMS_API + '/' + teamId,
    'PATCH',
    request,
    undefined,
    token,
  );
};

const buildPlayerRequest: (player: Player) => PlayerRequest = (player) => {
  return {
    id: player.id,
    position: player.position,
    status: player.status,
    playerOrder: player.playerOrder,
    playerOrderDestinationPitchArea: player.playerOrderDestinationPitchArea,
  };
};
