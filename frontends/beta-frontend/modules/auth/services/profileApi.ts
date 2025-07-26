import { connectorAPI } from '@/libs/fetcher';
import { DefaultResponse } from '@/shared/models/Responses';
import { ChangePasswordRequest } from '../types/requests/profileRequests';

const PROFILE_API = '/auth/profile';

export const getProfile = async (token: string): Promise<ProfileResponse> => {
  return connectorAPI<any>(PROFILE_API, 'GET', undefined, undefined, token);
};

export const changePassword = async (
  request: ChangePasswordRequest,
  token: string,
): Promise<DefaultResponse> => {
  return connectorAPI(
    '/auth/profile/change-password',
    'POST',
    request,
    'include',
    token,
  );
};

export const changeAvatar = async (
  request: FormData,
  token: string,
): Promise<DefaultResponse> => {
  return connectorAPI(
    '/auth/profile/avatar',
    'POST',
    request,
    'include',
    token,
    {
      'Content-Type': 'multipart/form-data',
    },
  );
};
