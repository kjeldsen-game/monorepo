import { useState } from 'react';
import {
  ForgetPasswordRequest,
  PasswordResetRequest,
  SignInRequest,
  SignUpRequest,
} from '../types/requests/authRequests';
import * as authApi from '../services/authApi';
import { useError } from '@/shared/contexts/ErrorContext';
import { useNotification } from '@/shared/contexts/NotificationContext';
import { signIn } from 'next-auth/react';
import { useRouter } from 'next/router';
import { DefaultResponse } from '@/shared/models/Responses';

export function useAuth() {
  const [loading, setLoading] = useState(false);
  const { setError } = useError();
  const router = useRouter();
  const { setNotification } = useNotification();

  const handleForgetPassword = async (
    request: ForgetPasswordRequest,
  ): Promise<void> => {
    setLoading(true);
    setError(null);
    try {
      const response: DefaultResponse = await authApi.forgetPassword(request);
      setNotification(response.message);
    } catch (error: any) {
      setError(error.message || 'Forget password failed');
    } finally {
      setLoading(false);
    }
  };

  const handleResetPassword = async (
    request: PasswordResetRequest,
  ): Promise<void> => {
    setLoading(true);
    setError(null);
    try {
      const response: DefaultResponse = await authApi.resetPassword(request);
      setNotification(response.message);
    } catch (error: any) {
      setError(error.message || 'Reset password failed');
    } finally {
      setLoading(false);
    }
  };

  const handleSignIn = async (request: SignInRequest): Promise<void> => {
    setError(null);
    setLoading(true);
    return new Promise((resolve, reject) => {
      const { email, password } = request;
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

  const handleSignUp = async (request: SignUpRequest): Promise<void> => {
    setLoading(true);
    setError(null);
    try {
      const response: DefaultResponse = await authApi.signUp(request);
      setNotification(response.message);
      await handleSignIn({
        email: request.email,
        password: request.password,
      });
    } catch (error: any) {
      setError(error.message || 'Register failed');
      setLoading(false);
    }
  };

  return {
    handleSignUp,
    handleSignIn,
    handleForgetPassword,
    handleResetPassword,
    loading,
  };
}
