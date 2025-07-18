import { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import { connectorAuth } from '@/libs/fetcher';
import { useError } from '@/shared/contexts/ErrorContext';
import { signIn } from 'next-auth/react';
import { useNotification } from '@/shared/contexts/NotificationContext';
import { DefaultResponse } from '@/shared/models/Responses';

export interface ForgetPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
  confirmPassword: string;
}

export function useAuth() {
  const { setNotification } = useNotification();
  const { setError } = useError();
  const router = useRouter();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const handleRouteChangeComplete = () => {
      setLoading(false);
    };
    router.events.on('routeChangeComplete', handleRouteChangeComplete);
    return () => {
      router.events.off('routeChangeComplete', handleRouteChangeComplete);
    };
  }, [router]);

  const useResetPassword = async (
    request: ResetPasswordRequest,
  ): Promise<void> => {
    setError(null);
    setLoading(true);
    try {
      const response: DefaultResponse = await connectorAuth(
        '/auth/reset-password',
        'POST',
        request,
      );
      setNotification(response.message);
    } catch (error: any) {
      setError(error.message || 'Password reset failed');
    } finally {
      setLoading(false);
    }
  };

  const useForgetPassword = async (
    request: ForgetPasswordRequest,
  ): Promise<void> => {
    setError(null);
    setLoading(true);
    try {
      const response: DefaultResponse = await connectorAuth(
        '/auth/forget-password',
        'POST',
        request,
      );
      setNotification(response.message);
    } catch (error: any) {
      setError(error.message || 'Password reset failed');
    } finally {
      setLoading(false);
    }
  };

  const apiSignIn = async (email: string, password: string): Promise<void> => {
    setError(null);
    setLoading(true);
    return new Promise((resolve, reject) => {
      signIn('credentials', {
        redirect: false,
        email: email,
        password: password,
      })
        .then((res: any) => {
          if (res?.status === 200) {
            resolve(res.status);
            router.push('/team');
          } else {
            setError(res?.error);
            reject('Invalid credentiasssls');
            setLoading(false);
          }
        })
        .catch((error) => {
          console.error(error);
          reject(error);
          setLoading(false);
        });
    });
  };

  const apiSignUp = async (
    email: string,
    password: string,
    confirmPassword: string,
    teamName: string,
  ): Promise<void> => {
    setLoading(true);
    setError(null);
    try {
      const response: DefaultResponse = await connectorAuth(
        '/auth/register',
        'POST',
        {
          email,
          password,
          confirmPassword,
          teamName,
        },
      );
      setNotification(response.message);
      const status = await apiSignIn(email, password);
      if (status == 200) {
        router.push('/');
      } else {
        throw new Error('Autologin failed');
      }
    } catch (error: any) {
      setError(error.message || 'Register failed');
      setLoading(false);
    }
  };

  return { apiSignIn, apiSignUp, useResetPassword, useForgetPassword, loading };
}
