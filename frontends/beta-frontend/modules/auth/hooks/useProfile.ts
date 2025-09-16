import useSWR from 'swr';
import { useError } from '@/shared/contexts/ErrorContext';
import { useNotification } from '@/shared/contexts/NotificationContext';
import {
  changeAvatar,
  changePassword,
  getProfile,
} from '../services/profileApi';
import { ChangePasswordRequest } from '../types/requests/profileRequests';
import { useSession } from 'next-auth/react';
import { useSnackbar } from 'notistack';

const API = '/auth/profile';

export const useProfile = () => {
  const { data: userData } = useSession();
  const { enqueueSnackbar } = useSnackbar();

  const {
    data,
    error,
    isLoading,
    mutate: refreshProfile,
  } = useSWR<ProfileResponse>(userData?.accessToken ? API : null, () =>
    getProfile(userData.accessToken!),
  );

  const handleChangePassword = async (
    request: ChangePasswordRequest,
  ): Promise<void> => {
    try {
      const response = await changePassword(request, userData.accessToken!);
      enqueueSnackbar(response.message, { variant: 'success' });
    } catch (err: any) {
      enqueueSnackbar(err.message || 'Password update failed', {
        variant: 'error',
      });
    }
  };

  const handleChangeAvatar = async (formData: FormData): Promise<void> => {
    try {
      const response = await changeAvatar(formData, userData.accessToken!);
      enqueueSnackbar(response.message, { variant: 'success' });
      refreshProfile();
    } catch (err: any) {
      enqueueSnackbar(err.message || 'Avatar update failed', {
        variant: 'error',
      });
    }
  };

  return {
    data,
    error,
    isLoading,
    handleChangePassword,
    handleChangeAvatar,
  };
};
