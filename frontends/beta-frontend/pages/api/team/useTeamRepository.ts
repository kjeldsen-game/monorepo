import { connectorAPI } from '@/libs/fetcher';
import { Team } from '@/shared/models/Team';
import useSWR from 'swr';
import { TeamPlayerPatchRequest } from './TeamPlayerPatchRequest';
import { Player } from '@/shared/models/Player';

const API = '/team/';

const fetcher = (teamId?: string, token?: string | null) => {
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
          };
        }
        return {
          id: player.id,
          position: player.position,
          status: player.status,
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

  const updateTeam = (value: Player[]): void => {
    if (!data) return;
    const oldData = data.players;
    const newData: TeamPlayerPatchRequest = {
      players: oldData.map((player) => {
        const updatedPlayer = value.find(
          (playerValue) => playerValue.id === player.id,
        );
        if (updatedPlayer && updatedPlayer.id === player.id) {
          return {
            id: updatedPlayer.id,
            position: updatedPlayer.position,
            status: updatedPlayer.status,
          };
        }
        return {
          id: player.id,
          position: player.position,
          status: player.status,
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
    )
      .then(() => {
        mutate();
      })
      .catch((error) => {
        console.error('Error updating auction:', error);
        throw error;
      });
  };

  return { data, error, isLoading, updateTeamPlayer, updateTeam };
};
export { useTeamRepository };
