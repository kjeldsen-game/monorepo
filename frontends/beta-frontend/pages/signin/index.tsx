'useClient';
import {
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  Snackbar,
  TextField,
} from '@mui/material';
import { Controller, useForm } from 'react-hook-form';
import Head from 'next/head';
import Link from 'next/link';
import { NextPageWithLayout } from '@/pages/_app';
import { CenterContainer } from '@/shared/layout';
import { apiSignIn } from '../api/auth/signup';
import { useRouter } from 'next/navigation';
import { useState } from 'react';

interface SignInFormValues {
  username: string;
  password: string;
}

const SignInPage: NextPageWithLayout = () => {
  const [open, setOpen] = useState(false);
  const router = useRouter();

  const { handleSubmit, control } = useForm<SignInFormValues>({
    mode: 'onBlur',
    reValidateMode: 'onChange',
  });

  const handleLogin = (username: string, password: string) => {
    apiSignIn(username, password)
      .then(() => {
        router.push('/');
      })
      .catch((error) => {
        console.error(error);
        setOpen(true);
      });
  };

  const handleCloseSnackBar = () => {
    setOpen(false);
  };

  return (
    <>
      <Head>
        <title>Sign In</title>
      </Head>
      <Card>
        <CardHeader title="Sign In" component={Box} />
        <CardContent>
          <form
            css={{
              display: 'flex',
              flexDirection: 'column',
              alignContent: 'center',
              justifyContent: 'center',
              rowGap: '1rem',
            }}
            onSubmit={handleSubmit(async (data) => {
              handleLogin(data.username, data.password);
            })}>
            <Controller
              name="username"
              control={control}
              defaultValue=""
              rules={{
                required: 'Username required',
              }}
              render={({
                field: { onChange, onBlur, value },
                fieldState: { error },
              }) => (
                <TextField
                  label="Username"
                  variant="filled"
                  value={value}
                  onChange={onChange}
                  onBlur={onBlur}
                  error={!!error}
                  helperText={error ? error.message : null}
                  type="text"
                />
              )}
            />
            <Controller
              name="password"
              control={control}
              defaultValue=""
              rules={{ required: 'Password required' }}
              render={({
                field: { onChange, onBlur, value },
                fieldState: { error },
              }) => (
                <TextField
                  label="Password"
                  variant="filled"
                  value={value}
                  onChange={onChange}
                  onBlur={onBlur}
                  error={!!error}
                  helperText={error ? error.message : null}
                  type="password"
                />
              )}
            />
            <div>
              <Button type="submit" variant="contained" color="primary">
                Login
              </Button>
            </div>
          </form>
          <a href="/signup">Create an account</a>
        </CardContent>
      </Card>
      <Snackbar
        open={open}
        autoHideDuration={6000}
        onClose={handleCloseSnackBar}
        message="Login failed"
      />
    </>
  );
};

SignInPage.getLayout = (page) => <CenterContainer>{page}</CenterContainer>;

export default SignInPage;
