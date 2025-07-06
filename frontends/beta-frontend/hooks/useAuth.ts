import { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import { connectorAuth } from '@/libs/fetcher';
import { useError } from '@/shared/contexts/ErrorContext';
import { signIn } from 'next-auth/react';

export function useAuth() {
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

  const apiSignUp = async (
    email: string,
    password: string,
    confirmPassword: string,
    teamName: string,
  ): Promise<void> => {
    setLoading(true);
    setError(null);

    try {
      await connectorAuth('/auth/register', 'POST', {
        email,
        password,
        confirmPassword,
        teamName,
      });

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

  return { apiSignIn, apiSignUp, loading };
}
