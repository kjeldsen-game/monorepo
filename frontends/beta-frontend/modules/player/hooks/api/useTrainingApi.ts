import { useSession } from 'next-auth/react';
import useSWR, { useSWRConfig } from 'swr';
import { TEAMS_API } from '../../services/teamApi';
import { useSnackbar } from 'notistack';
import * as trainingApi from 'modules/player/services/trainingApi';
import { ScheduleTrainingRequest } from 'modules/player/types/TrainingRequests';
import { TeamTrainingEventsResponse } from 'modules/player/types/TrainingResponses';

export const useTrainingApi = () => {
  const { data: userData } = useSession();
  const { enqueueSnackbar } = useSnackbar();
  const { mutate: mutateActiveTrainings } = useSWRConfig();

  const { data, error, isLoading } = useSWR<TeamTrainingEventsResponse>(
    userData?.accessToken
      ? TEAMS_API + userData?.user?.teamId + 'historical'
      : null,
    () =>
      trainingApi.getTrainings(userData?.user.teamId, userData?.accessToken),
  );

  const handleScheduleTrainingRequest = async (
    request: ScheduleTrainingRequest,
    playerId: string,
  ): Promise<void> => {
    try {
      await trainingApi.scheduleTraining(
        playerId,
        request,
        userData?.accessToken,
      );
      enqueueSnackbar('Training was successfully scheduled', {
        variant: 'success',
      });
      mutateActiveTrainings(TEAMS_API + userData?.user?.teamId + 'active');
    } catch (err: any) {
      enqueueSnackbar(err.message);
    }
  };

  return {
    data,
    error,
    isLoading,
    handleScheduleTrainingRequest,
  };
};
