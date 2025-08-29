import { useSession } from 'next-auth/react';
import useSWR, { useSWRConfig } from 'swr';
import * as teamsApi from '../../services/teamApi';
import { useSnackbar } from 'notistack';
import { TeamPlayerPatchRequest } from 'modules/player/types/Requests';

export const useTeamApi = (teamId?: string) => {
  const { mutate: mutateTeamValidation } = useSWRConfig();
  const { enqueueSnackbar } = useSnackbar();
  const { data: userData } = useSession();

  const { data, error, isLoading, mutate } = useSWR(
    userData?.accessToken
      ? teamsApi.TEAMS_API + (teamId ? teamId : userData?.user?.teamId)
      : null,
    () =>
      teamsApi.getTeam(
        teamId ? teamId : userData?.user?.teamId,
        userData?.accessToken,
      ),
    {
      onSuccess: () =>
        mutateTeamValidation(
          teamsApi.TEAMS_API +
            'validation' +
            (teamId ? teamId : userData?.user?.teamId),
        ),
    },
  );

  const handleLineupUpdateRequest = async (request): Promise<void> => {
    try {
      const response = await teamsApi.updateLineup(
        userData?.user?.teamId,
        userData?.accessToken,
        data?.players,
        request.players,
        request.teamModifiers,
      );
      enqueueSnackbar('Team was successfully updated', { variant: 'success' });
      mutate();
      mutateTeamValidation(
        teamsApi.TEAMS_API + 'validation' + userData?.user?.teamId,
      );
    } catch (err: any) {
      enqueueSnackbar(err.message);
    }
  };

  return {
    data,
    error,
    isLoading,
    handleLineupUpdateRequest,
  };
};
