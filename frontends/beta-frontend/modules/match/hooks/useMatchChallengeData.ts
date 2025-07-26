import { useSession } from 'next-auth/react';
import useSWR from 'swr';
import { getUserMatchChallenges } from '../services/matchApi';

export const MATCH_CHALLENGE_API = '/match';

export const useMatchChallengeData = () => {
  const { data: userData } = useSession();

  const { data, error, isLoading } = useSWR(
    userData?.accessToken
      ? MATCH_CHALLENGE_API + userData?.user.teamId + '/challenges'
      : null,
    () => getUserMatchChallenges(userData.accessToken!, userData.user.teamId!),
  );

  const pastMatches = data?.filter((match) => match.status === 'PLAYED');
  const incomingMatches = data?.filter(
    (match) => new Date(match.dateTime).getTime() > new Date().getTime(),
  );
  const acceptedMatches = data?.filter((match) => match.status === 'ACCEPTED');
  const pendingMatches = data?.filter((match) => match.status === 'PENDING');

  return {
    pastMatches,
    incomingMatches,
    acceptedMatches,
    pendingMatches,
    error,
    isLoading,
  };
};
