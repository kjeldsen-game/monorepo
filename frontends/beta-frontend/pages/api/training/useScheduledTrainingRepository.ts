import { connectorAPI } from '@/libs/fetcher';
import { PlayerSkill } from '@/shared/models/PlayerSkill';
import useSWR from 'swr';

const API = '/training/';

const fetcher = (teamId: string | null, token: string | null) => {
  if (token === null || teamId === null) {
    return undefined;
  }
  return connectorAPI<any>(
    API + teamId + '/scheduled',
    'GET',
    undefined,
    undefined,
    token,
  );
};

const useScheduledTrainingRepository = (
  teamId?: string,
  token?: string,
  playerId?: string,
) => {
  const { data, error, isLoading, mutate } = useSWR<any>(
    token ? API + teamId + '/scheduled' : null,
    () => fetcher(teamId ? teamId : null, token ? token : null),
  );

  const scheduleTraining = (skill: PlayerSkill): Promise<any> => {
    if (playerId) Promise.reject();
    const newData = {
      skill: skill,
    };
    return connectorAPI<any>(
      `${API}${playerId}`,
      'POST',
      newData,
      'include',
      token,
    )
      .then((response) => {
        mutate();
        return response;
      })
      .catch((error) => {
        console.error('Error scheduling training:', error);
        throw error;
      });
  };

  return { data, isLoading, error, scheduleTraining };
};

export { useScheduledTrainingRepository };
