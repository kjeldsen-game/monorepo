import { useSession } from 'next-auth/react';
import useSWR from 'swr';
import * as matchApi from '../services/matchApi';
import { useRouter } from 'next/router';
import { MATCH_CHALLENGE_API } from './useMatchChallengeActions';

export const useMatchFormation = (selfChallenge: boolean) => {
  const { data: userData } = useSession();
  const matchId = useRouter().query.id as string;

  const { data, error, isLoading, mutate } = useSWR(
    userData?.accessToken
      ? `${MATCH_CHALLENGE_API}/${matchId}/teams/${userData?.user.teamId}/validate`
      : null,
    () =>
      matchApi.validateMatchTeams(
        userData.user.teamId!,
        matchId,
        userData.accessToken!,
        selfChallenge,
      ),
  );

  return {
    data,
    error,
    mutate,
    isLoading,
  };
};
