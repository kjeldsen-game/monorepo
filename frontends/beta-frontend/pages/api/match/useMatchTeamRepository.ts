import { connectorAPI } from '@/libs/fetcher';
import { MatchReport } from '@/shared/models/MatchReport';
import useSWR from 'swr';
import { TeamPlayerPatchRequest } from '../team/TeamPlayerPatchRequest';
import { Player } from '@/shared/models/Player';
import { TeamModifiers } from '@/shared/models/TeamModifiers';

const API = '/match';

const fetcher = (
  matchId: string | null | string[],
  token: string | null,
  teamId: string | null | string[],
) => {
  if (token === null || matchId === null || teamId === null) {
    // console.log('return undefined');
    return undefined;
  }
  return connectorAPI<any>(
    `${API}/${matchId}/team/${teamId}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};

const useMatchTeamRepository = (
  matchId?: string | undefined | string[],
  teamId?: string | undefined | string[],
  token?: string,
) => {
  const {
    data: matchTeam,
    isLoading,
    error,
    mutate,
  } = useSWR<any | undefined>(
    token ? `${API}/${matchId}/team/${teamId}` : null,
    () =>
      fetcher(
        matchId ? matchId : null,
        token ? token : null,
        teamId ? teamId : null,
      ),
  );

  const updateMatchTeam = (
    value: Player[],
    teamModifiers: TeamModifiers,
  ): Promise<any> => {
    if (!matchTeam) {
      return Promise.reject(new Error('Data is not available.'));
    }
    if (!matchId) {
      return Promise.reject(new Error('Match is not defined.'));
    }
    if (!teamId) {
      return Promise.reject(new Error('Team is not defined.'));
    }
    const newData: TeamPlayerPatchRequest = {
      players: value.map((player) => ({
        id: player.id,
        position: player.position,
        status: player.status,
        playerOrder: player.playerOrder,
        playerOrderDestinationPitchArea: player.playerOrderDestinationPitchArea,
      })),
      teamModifiers,
    };

    return connectorAPI<TeamPlayerPatchRequest>(
      `${API}/${matchId}/team/${teamId}`,
      'PUT',
      newData,
      undefined,
      token,
    )
      .then((response) => {
        mutate();
        return response;
      })
      .catch((error) => {
        console.error('Error updating Team:', error);
        throw error;
      });
  };

  return { updateMatchTeam, matchTeam, isLoading, error };
};

export { useMatchTeamRepository };
