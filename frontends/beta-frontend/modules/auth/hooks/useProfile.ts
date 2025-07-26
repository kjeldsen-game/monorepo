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

const API = '/auth/profile';

export const useProfile = () => {
  const { data: userData } = useSession();
  const { setError } = useError();
  const { setNotification } = useNotification();

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
      setNotification(response.message);
    } catch (err: any) {
      setError(err.message || 'Password update failed');
    }
  };

  const handleChangeAvatar = async (formData: FormData): Promise<void> => {
    try {
      const response = await changeAvatar(formData, userData.accessToken!);
      setNotification(response.message);
      refreshProfile();
    } catch (err: any) {
      setError(err.message || 'Avatar update failed');
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
