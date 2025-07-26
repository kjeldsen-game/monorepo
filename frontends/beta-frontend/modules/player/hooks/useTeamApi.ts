import { useSession } from 'next-auth/react';
import useSWR from 'swr';
import { getTeam, TEAMS_API } from '../services/teamApi';

export const useTeamApi = () => {
  const { data: userData } = useSession();

  const { data, error, isLoading } = useSWR(
    userData?.accessToken ? TEAMS_API + userData?.user?.teamId : null,
    () => getTeam(userData?.user?.teamId, userData?.accessToken),
  );

  return {
    data,
    error,
    isLoading,
  };
};
