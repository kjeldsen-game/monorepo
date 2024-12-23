import { connectorAPI } from '@/libs/fetcher';
import { Team } from '@/shared/models/Team';
import useSWR from 'swr';
import { TeamPlayerPatchRequest } from './TeamPlayerPatchRequest';
import { Player } from '@/shared/models/Player';

const API = '/team/';

const fetcher = (teamId?: string, token?: string | null) => {
  console.log(teamId);
  console.log(token);
  if (token === null) {
    return undefined;
  }
  return connectorAPI<any>(API + teamId, 'GET', undefined, undefined, token);
};

const useTeamRepository = (team?: string, token?: string) => {
  const { data, mutate, error, isLoading } = useSWR<Team | undefined>(
    team ? API + team : null,
    () => fetcher(team, token ? token : null),
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

  const updateTeam = (value: Player[]): Promise<any> => {
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
