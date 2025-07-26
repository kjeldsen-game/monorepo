import { useSession } from 'next-auth/react';
import useSWR from 'swr';
import * as leagueApi from '../services/leagueApi';
import * as matchLeagueApi from '../services/matchLeagueApi';
import { LeagueResponse } from '../types/LeagueResponses';
import { MATCH_CHALLENGE_API } from './useMatchChallengeActions';

export const useLeagueApi = (leagueId: string) => {
  const { data: userData } = useSession();

  const { data, error, isLoading } = useSWR<LeagueResponse>(
    leagueId ? leagueApi.LEAGUE_API + leagueId : null,
    () =>
      leagueApi.getLeague(
        leagueId ? leagueId : null,
        userData?.accessToken ? userData?.accessToken : null,
      ),
  );

  const { data: matches } = useSWR<any>(
    leagueId ? MATCH_CHALLENGE_API + leagueId : null,
    () =>
      matchLeagueApi.getLeagueMatches(
        0,
        99,
        leagueId ? leagueId : null,
        userData?.accessToken ? userData?.accessToken : null,
      ),
  );

  return {
    matches,
    data,
    error,
    isLoading,
  };
};
