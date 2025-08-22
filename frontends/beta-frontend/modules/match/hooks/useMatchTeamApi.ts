import { useSession } from 'next-auth/react';
import useSWR from 'swr';
import * as matchTeamApi from '../services/matchTeamApi';
import { useRouter } from 'next/router';
import { MATCH_API } from '../services/constants';
import { useState } from 'react';
import { TeamPlayerPatchRequest } from 'modules/player/types/Requests';
import { useSnackbar } from 'notistack';

export const useMatchTeamApi = (self: boolean) => {
  const { data: userData } = useSession();
  const [loading, setLoading] = useState(false);
  const { enqueueSnackbar } = useSnackbar();
  const matchId = useRouter().query.id as string;

  const { data, error, isLoading, mutate } = useSWR<any>(
    userData?.accessToken
      ? `${MATCH_API}/${matchId}/team/${userData?.user?.teamId}`
      : null,
    () =>
      matchTeamApi.getMatchTeamByTeamIdAndMatchId(
        matchId,
        userData?.user?.teamId,
        userData?.accessToken,
      ),
  );

  const handleUpdateMatchTeamByTeamIdAndMatchId = async (
    request: TeamPlayerPatchRequest,
  ): Promise<void> => {
    request.self = self;
    setLoading(true);
    try {
      const response = await matchTeamApi.updateMatchTeamByTeamIdAndMatchId(
        matchId,
        userData?.user?.teamId,
        userData?.accessToken,
        request,
      );
      enqueueSnackbar(
        response.message || 'Team for the match was successfully updated!',
      );
    } catch (error: any) {
      enqueueSnackbar(error.message || 'Update of team failed!');
    } finally {
      setLoading(false);
    }
  };

  return {
    data,
    error,
    mutate,
    isLoading,
    handleUpdateMatchTeamByTeamIdAndMatchId,
    loading,
  };
};
