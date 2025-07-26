import { useSession } from 'next-auth/react';
import useSWR from 'swr';
import { getTeams, TEAMS_API } from '../services/teamApi';

export const useTeamsApi = () => {
  const { data: userData } = useSession();

  const { data, error, isLoading } = useSWR(
    userData?.accessToken ? TEAMS_API : null,
    () => getTeams(userData.accessToken!),
  );

  return {
    data,
    error,
    isLoading,
  };
};
