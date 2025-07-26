import { useSession } from 'next-auth/react';
import { useSWRConfig } from 'swr';
import { useError } from '@/shared/contexts/ErrorContext';
import { useState } from 'react';
import { useNotification } from '@/shared/contexts/NotificationContext';
import * as matchApi from '../services/matchApi';
import { DefaultResponse } from '@/shared/models/Responses';

export const MATCH_CHALLENGE_API = '/match';

export const useMatchChallengeActions = () => {
  const [loading, setLoading] = useState(false);
  const { data: userData } = useSession();
  const { setError } = useError();
  const { setNotification } = useNotification();
  const { mutate } = useSWRConfig();

  const handleMatchChallengeAction = async (
    action: () => Promise<DefaultResponse>,
  ): Promise<void> => {
    setLoading(true);
    setError(null);
    try {
      const response = await action();
      setNotification(response.message);
      mutate(MATCH_CHALLENGE_API + userData?.user.teamId + '/challenges');
    } catch (error: any) {
      setError(error.message || 'Something went wrong');
    } finally {
      setLoading(false);
    }
  };

  const handleMatchAccept = async (matchId: string): Promise<void> => {
    handleMatchChallengeAction(() =>
      matchApi.acceptMatch(userData?.accessToken, matchId),
    );
  };

  const handleMatchDecline = async (matchId: string): Promise<void> => {
    handleMatchChallengeAction(() =>
      matchApi.declineMatch(userData?.accessToken, matchId),
    );
  };

  const handleMatchCreate = async (awayId: string): Promise<void> => {
    handleMatchChallengeAction(() =>
      matchApi.createMatch(
        userData?.accessToken,
        userData?.user.teamId,
        awayId,
      ),
    );
  };

  const handleMatchExecute = async (matchId: string): Promise<void> => {
    handleMatchChallengeAction(() =>
      matchApi.executeMatch(userData?.accessToken, matchId),
    );
  };

  return {
    handleMatchAccept,
    handleMatchCreate,
    handleMatchDecline,
    handleMatchExecute,
    loading,
  };
};
