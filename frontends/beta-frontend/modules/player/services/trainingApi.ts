import { connectorAPI } from '@/libs/fetcher';
import { ScheduleTrainingRequest } from '../types/TrainingRequests';
import { DefaultResponse } from '@/shared/models/Responses';
import {
  PlayerScheduledTraningResponse,
  TeamTrainingEventsResponse,
} from '../types/TrainingResponses';

const TRAINING_API = '/training';

export const getTrainings = (
  teamId: string | null,
  token: string | null,
): Promise<TeamTrainingEventsResponse> => {
  if (token === undefined || teamId === undefined) {
    return undefined;
  }
  return connectorAPI<undefined>(
    `${TRAINING_API}/${teamId}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};

export const getActiveTrainings = (
  teamId: string | null,
  token: string | null,
): Promise<PlayerScheduledTraningResponse[]> => {
  if (token === null || teamId === null) {
    return undefined;
  }
  return connectorAPI(
    `${TRAINING_API}/${teamId}/scheduled`,
    'GET',
    undefined,
    undefined,
    token,
  );
};

export const scheduleTraining = (
  playerId: string,
  request: ScheduleTrainingRequest,
  token: string,
): Promise<DefaultResponse> => {
  return connectorAPI<ScheduleTrainingRequest>(
    `${TRAINING_API}/${playerId}`,
    'POST',
    request,
    'include',
    token,
  );
};
