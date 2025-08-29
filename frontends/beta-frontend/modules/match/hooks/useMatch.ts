import { useSession } from 'next-auth/react';
import useSWR from 'swr';
import * as matchApi from '../services/matchApi';
import { useRouter } from 'next/router';
import { MATCH_API } from '../services/constants';
import { MatchResponse } from '../types/MatchResponses';

export const useMatch = () => {
  const { data: userData } = useSession();
  const matchId = useRouter().query.id as string;

  const { data, error, isLoading, mutate } = useSWR<MatchResponse>(
    userData?.accessToken
      ? `${MATCH_API}/${matchId}/${userData?.user.teamId}`
      : null,
    () => matchApi.getMatchById(matchId, userData?.accessToken),
  );

  const refetch = () => {
    mutate();
  };

  return {
    data,
    error,
    mutate,
    refetch,
    isLoading,
  };
};
