import { getTeamsPageable } from "modules/player/services/teamApi";
import { useSession } from "next-auth/react";
import useSWR from "swr";

export const usePageableTeamsApi = (page?: number, size?: number) => {
  const { data: userData } = useSession();

  const { data, error, isLoading } = useSWR(
    userData?.accessToken ? `/team/pageable?page=${page}&size=${size}` : null,
    () => getTeamsPageable(userData.accessToken!, page, size),
  );

  return {
    data,
    error,
    isLoading,
  };
};