import { useSWRConfig } from 'swr';
import { MatchCreationRequest } from './MatchCreationRequest';
import { connectorAPI } from '@/libs/fetcher';
import { Match } from '@/shared/models/Match';

const API = '/match';

const useMatchRepository = (token: string, ownTeamId?: string) => {
  const { mutate } = useSWRConfig();

  const _patchMatch = async (matchId: string, params: Partial<Match>) => {
    const response = await connectorAPI<Partial<Match>>(
      API + '/' + matchId,
      'PATCH',
      params,
      undefined,
      token,
    );
    console.log(response);

    mutate([API, ownTeamId]);
    return response;
  };

  const createMatch = async (rivalTeamId: string) => {
    if (!ownTeamId) return;

    const newData: MatchCreationRequest = {
      home: { id: ownTeamId },
      away: { id: rivalTeamId },
    };

    const response = await connectorAPI<MatchCreationRequest>(
      API,
      'POST',
      newData,
      undefined,
      token,
    );

    mutate([API, ownTeamId]);
    return response;
  };

  const executeMatch = async (matchId: string) => {
    return _patchMatch(matchId, { status: 'SCHEDULED' });
  };

  const acceptMatch = async (matchId: string) => {
    _patchMatch(matchId, { status: 'ACCEPTED' });
  };

  const declineMatch = async (matchId: string) => {
    return _patchMatch(matchId, { status: 'REJECTED' });
  };

  return {
    executeMatch,
    createMatch,
    acceptMatch,
    declineMatch,
  };
};

export { useMatchRepository };
