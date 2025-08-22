import { connectorAPI } from '@/libs/fetcher';
import { MatchStatus } from '@/shared/models/match/Match';
import { CreateMatchRequest, PatchMatchRequest } from '../types/MatchRequests';
import { DefaultResponse } from '@/shared/models/Responses';
import { TeamFormationValiation } from '@/shared/models/player/Team';

const MATCH_API = '/match';

export const getUserMatchChallenges = async (
  token: string,
  teamId: string,
): Promise<any> => {
  return connectorAPI<any>(
    `${MATCH_API}?page=${0}&size=${99}&teamId=${teamId}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};

export const createMatch = async (
  token: string,
  ownTeamId: string,
  rivalTeamId: string,
) => {
  const newData: CreateMatchRequest = {
    home: { id: ownTeamId },
    away: { id: rivalTeamId },
  };

  const response = await connectorAPI<CreateMatchRequest>(
    MATCH_API,
    'POST',
    newData,
    undefined,
    token,
  );
  return response;
};

const _patchMatch = async (
  token: string,
  matchId: string,
  request: PatchMatchRequest,
): Promise<DefaultResponse> => {
  console.log(matchId);
  const response = await connectorAPI<PatchMatchRequest>(
    MATCH_API + '/' + matchId,
    'PATCH',
    request,
    undefined,
    token,
  );
  return response;
};

export const executeMatch = async (token: string, matchId: string) => {
  return _patchMatch(token, matchId, { status: MatchStatus.SCHEDULED });
};

export const acceptMatch = async (token: string, matchId: string) => {
  return _patchMatch(token, matchId, { status: MatchStatus.ACCEPTED });
};

export const declineMatch = async (token: string, matchId: string) => {
  return _patchMatch(token, matchId, { status: MatchStatus.REJECTED });
};

export const validateMatchTeams = async (
  teamId?: string,
  matchId?: string,
  token?: string,
  selfChallenge: boolean = false,
): Promise<TeamFormationValiation[]> => {
  if (token === undefined || matchId === undefined || teamId === undefined) {
    return undefined;
  }
  return connectorAPI<any>(
    `${MATCH_API}/${matchId}/teams/${teamId}/validate?selfChallenge=${selfChallenge}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};

export const getMatchById = async (matchId: string, token?: string) => {
  if (token === undefined || matchId === undefined) {
    return undefined;
  }
  console.log('Fetching match by ID:', matchId);
  return connectorAPI<any>(
    `${MATCH_API}/${matchId}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};
