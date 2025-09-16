import { useState } from 'react';
import {
  ForgetPasswordRequest,
  PasswordResetRequest,
  SignInRequest,
  SignUpRequest,
} from '../types/requests/authRequests';
import * as authApi from '../services/authApi';
import { signIn } from 'next-auth/react';
import { useRouter } from 'next/router';
import { DefaultResponse } from '@/shared/models/Responses';
import { useSnackbar } from 'notistack';

export function useAuth() {
  const [loading, setLoading] = useState(false);
  const router = useRouter();
  const { enqueueSnackbar } = useSnackbar();

  const handleForgetPassword = async (
    request: ForgetPasswordRequest,
  ): Promise<void> => {
    setLoading(true);
    try {
      const response: DefaultResponse = await authApi.forgetPassword(request);
      enqueueSnackbar(response.message, { variant: 'success' });
    } catch (error: any) {
      enqueueSnackbar(error.message || 'Forget password failed', {
        variant: 'error',
      });
    } finally {
      setLoading(false);
    }
  };

  const handleResetPassword = async (
    request: PasswordResetRequest,
  ): Promise<void> => {
    setLoading(true);
    try {
      const response: DefaultResponse = await authApi.resetPassword(request);
      enqueueSnackbar(response.message, { variant: 'success' });
    } catch (error: any) {
      enqueueSnackbar(error.message || 'Reset password failed', {
        variant: 'error',
      });
    } finally {
      setLoading(false);
    }
  };

  const handleSignIn = async (request: SignInRequest): Promise<void> => {
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
            enqueueSnackbar(res?.error, { variant: 'error' });
            reject('Invalid credentials');
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
    try {
      const response: DefaultResponse = await authApi.signUp(request);
      enqueueSnackbar(response.message, { variant: 'success' });
      await handleSignIn({
        email: request.email,
        password: request.password,
      });
    } catch (error: any) {
      enqueueSnackbar(error.message || 'Register failed', { variant: 'error' });
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
