import { connectorAuth } from '@/libs/fetcher';
import { DefaultResponse } from '@/shared/models/Responses';
import {
  ForgetPasswordRequest,
  PasswordResetRequest,
  SignUpRequest,
} from '../types/requests/authRequests';

export const resetPassword = async (
  request: PasswordResetRequest,
): Promise<DefaultResponse> => {
  return connectorAuth('/auth/reset-password', 'POST', request);
};

export const forgetPassword = async (
  request: ForgetPasswordRequest,
): Promise<DefaultResponse> => {
  return connectorAuth('/auth/forget-password', 'POST', request);
};

export const signUp = async (
  request: SignUpRequest,
): Promise<DefaultResponse> => {
  return connectorAuth('/auth/register', 'POST', request);
};
