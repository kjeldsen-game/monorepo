import { connectorAPI } from '@/libs/fetcher';
import { Team } from '@/shared/models/Team';
import useSWR from 'swr';
import { TeamPlayerPatchRequest } from './TeamPlayerPatchRequest';
import { Player } from '@/shared/models/player/Player';
import { TeamModifiers } from '@/shared/models/player/TeamModifiers';

const API = '/team/';

const fetcher = (teamId?: string | null | string[], token?: string | null) => {
  if (token === null || teamId === null) {
    return undefined;
  }
  return connectorAPI<any>(API + teamId, 'GET', undefined, undefined, token);
};

const useTeamRepository = (team?: string, token?: string) => {
  const { data, mutate, error, isLoading } = useSWR<Team | undefined>(
    token ? API + team : null,
    () => fetcher(team ? team : null, token ? token : null),
  );

  const updateTeamPlayer = (value: Player): void => {
    if (!data) return;
    const oldData = data.players;
    const newData: TeamPlayerPatchRequest = {
      players: oldData.map((player) => {
        if (player.id === value.id) {
          return {
            id: value.id,
            position: value.position,
            status: value.status,
            playerOrder: value.playerOrder,
          };
        }
        return {
          id: player.id,
          position: player.position,
          status: player.status,
          playerOrder: player.playerOrder,
        };
      }),
    };

    if (!team) return;
    connectorAPI<TeamPlayerPatchRequest>(
      API + team,
      'PATCH',
      newData,
      undefined,
      token,
    ).then(() => {
      mutate();
    });
  };

  const updateTeam = (
    value: Player[],
    teamModifiers: TeamModifiers,
  ): Promise<any> => {
    if (!data) {
      return Promise.reject(new Error('Data is not available.'));
    }

    if (!team) {
      return Promise.reject(new Error('Team is not defined.'));
    }

    const oldData = data.players;
    const newData: TeamPlayerPatchRequest = {
      players: oldData.map((player) => {
        const updatedPlayer = value.find(
          (playerValue) => playerValue.id === player.id,
        );

        if (updatedPlayer) {
          return {
            id: updatedPlayer.id,
            position: updatedPlayer.position,
            status: updatedPlayer.status,
            playerOrder: updatedPlayer.playerOrder,
            playerOrderDestinationPitchArea:
              updatedPlayer.playerOrderDestinationPitchArea,
          };
        }
        return {
          id: player.id,
          position: player.position,
          status: player.status,
          playerOrder: player.playerOrder,
          playerOrderDestinationPitchArea:
            player.playerOrderDestinationPitchArea,
        };
      }),
      teamModifiers,
    };

    return connectorAPI<TeamPlayerPatchRequest>(
      API + team,
      'PATCH',
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

  return { data, error, isLoading, updateTeamPlayer, updateTeam };
};
export { useTeamRepository };
