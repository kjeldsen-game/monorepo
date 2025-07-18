import { connectorAPI } from '@/libs/fetcher';
import { useError } from '@/shared/contexts/ErrorContext';
import { useNotification } from '@/shared/contexts/NotificationContext';
import { useEffect, useState } from 'react';
import useSWR, { mutate } from 'swr';

const API = '/auth/profile';

export interface ChangePasswordRequest {
  oldPassword: string;
  newPassword: string;
  confirmPassword: string;
}

const fetcher = (token: string | null) => {
  if (token === null) {
    return undefined;
  }
  return connectorAPI<any>(API, 'GET', undefined, undefined, token);
};

const useProfileRepository = (token?: string) => {
  const { setError } = useError();
  const { setNotification } = useNotification();

  const { data, error, isLoading, mutate } = useSWR<any>(
    token ? API : null,
    () => fetcher(token ? token : null),
  );

  const changePassword = (request: ChangePasswordRequest): Promise<any> => {
    return connectorAPI<any>(
      '/auth/profile/change-password',
      'POST',
      request,
      'include',
      token,
    )
      .then((response) => {
        setNotification('Password successfully changed!');
        return response;
      })
      .catch((error) => {
        setError(error.message);
      });
  };

  const changeAvatar = (request: FormData): Promise<any> => {
    return connectorAPI<FormData>(
      '/auth/profile/avatar',
      'POST',
      request,
      'include',
      token,
      {
        'Content-type':
          'multipart/form-data; boundary=----WebKitFormBoundaryQdiJuMFAyJ22cHcB',
      },
    )
      .then((response) => {
        setNotification('Avatar successfully updated!');
        mutate();
        return response;
      })
      .catch((error) => {
        setError(error.message);
      });
  };

  return { data, error, changePassword, changeAvatar };
};

export { useProfileRepository };
