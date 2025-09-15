import { useSession } from 'next-auth/react';
import useSWR from 'swr';
import { TEAMS_API } from '../../services/teamApi';
import * as trainingApi from 'modules/player/services/trainingApi';
import { PlayerScheduledTraningResponse } from 'modules/player/types/TrainingResponses';

export const useActiveTrainingApi = () => {
  const { data: userData } = useSession();
  const { data, error, isLoading } = useSWR<PlayerScheduledTraningResponse[]>(
    userData?.accessToken
      ? TEAMS_API + userData?.user?.teamId + 'active'
      : null,
    () =>
      trainingApi.getActiveTrainings(
        userData?.user.teamId,
        userData?.accessToken,
      ),
  );

  return {
    data,
    error,
    isLoading,
  };
};
