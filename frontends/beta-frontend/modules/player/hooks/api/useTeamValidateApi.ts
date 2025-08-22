import { useSession } from 'next-auth/react';
import useSWR from 'swr';
import * as teamsApi from '../../services/teamApi';

export const useTeamValidateApi = () => {
  const { data: userData } = useSession();

  const { data, error, isLoading, mutate } = useSWR(
    userData?.accessToken
      ? teamsApi.TEAMS_API + 'validation' + userData?.user?.teamId
      : null,
    () =>
      teamsApi.getLineupValidation(
        userData?.user?.teamId,
        userData?.accessToken,
      ),
  );

  return {
    data,
    error,
    isLoading,
    mutate,
  };
};
