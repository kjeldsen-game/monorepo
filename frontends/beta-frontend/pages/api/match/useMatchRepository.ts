import { useSWRConfig } from 'swr';
import { MatchCreationRequest } from './MatchCreationRequest';
import { connectorAPI } from '@/libs/fetcher';
import { Match } from '@/shared/models/Match';

const API = '/match';

const useMatchRepository = (token: string, ownTeamId?: string) => {
  const { mutate } = useSWRConfig();

  const _patchMatch = async (matchId: string, params: Partial<Match>) => {
    connectorAPI<Partial<Match>>(
      API + '/' + matchId,
      'PATCH',
      params,
      undefined,
      token,
    ).then(() => {
      mutate([API, ownTeamId]);
    });
  };

  const createMatch = async (rivalTeamId: string, date: Date) => {
    if (!ownTeamId) return;
    const newData: MatchCreationRequest = {
      home: {
        id: ownTeamId,
      },
      away: {
        id: rivalTeamId,
      },
      dateTime: date,
    };
    connectorAPI<MatchCreationRequest>(
      API,
      'POST',
      newData,
      undefined,
      token,
    ).then(() => {
      mutate([API, ownTeamId]);
    });
  };

  const executeMatch = async (matchId: string) => {
    _patchMatch(matchId, { status: 'SCHEDULED' });
  };

  const acceptMatch = async (matchId: string) => {
    _patchMatch(matchId, { status: 'ACCEPTED' });
  };

  const declineMatch = async (matchId: string) => {
    _patchMatch(matchId, { status: 'REJECTED' });
  };

  return {
    executeMatch,
    createMatch,
    acceptMatch,
    declineMatch,
  };
};

export { useMatchRepository };
