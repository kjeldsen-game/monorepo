'useClient';
import {
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  Snackbar,
  TextField,
  Toolbar,
} from '@mui/material';
import { Controller, useForm } from 'react-hook-form';
import Head from 'next/head';
import Link from 'next/link';
import { NextPageWithLayout } from '@/pages/_app';
import { Layout } from '@/shared/layout';
import { apiSignIn } from '../api/auth/signup';
import { useRouter } from 'next/navigation';
import { useState } from 'react';
import { standardPinkTextFieldSx } from '../signup';
import MarketButton from '@/shared/components/Market/MarketButton';
import LinkButton from '@/shared/components/Common/LinkButton';

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
        router.push('/team');
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
      <Card sx={{ width: { xs: '60%', sm: '60%', md: '40%' }, boxShadow: 24 }}>
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
                  sx={standardPinkTextFieldSx}
                  label="Username"
                  variant="standard"
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
                  sx={standardPinkTextFieldSx}
                  label="Password"
                  variant="standard"
                  value={value}
                  onChange={onChange}
                  onBlur={onBlur}
                  error={!!error}
                  helperText={error ? error.message : null}
                  type="password"
                />
              )}
            />
            <div style={{ display: 'flex', justifyContent: 'center' }}>
              <MarketButton type="submit" variant="contained" color="primary">
                Login
              </MarketButton>
            </div>
          </form>
          <Box
            sx={{
              width: '100%',
              display: 'flex',
              justifyContent: 'center',
              paddingTop: '8px',
              flexDirection: 'column',
              alignContent: 'center',
              alignItems: 'center'
            }}>
            No account yet?
            <LinkButton sx={{ marginTop: '8px' }} variant='contained' link="/signup">Create an account</LinkButton>
            {/* <Link href="/signup" passHref style={{ textDecoration: 'none', color: 'black' }}>
            </Link> */}
          </Box>
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

SignInPage.getLayout = (page) => (
  <Layout isMenu={false}>
    <Box
      sx={{
        mt: { xs: '-112px', sm: '-128px' },
        position: 'relative',
        width: '100vw',
        height: '100vh',
        overflow: 'hidden',
      }}>
      <Box
        sx={{
          position: 'absolute',
          top: '50%',
          left: '50%',
          width: { xs: '95vh', md: '95vw', sm: '140vw' },
          height: { xs: '95vh', md: '95vh', sm: '140vw' },
          backgroundImage:
            'linear-gradient(rgba(255,255,255,0.8), rgba(255,255,255,0.8)), url("/img/testss.svg")',
          backgroundRepeat: 'no-repeat',
          backgroundPosition: 'center',
          backgroundSize: 'contain',
          transformOrigin: 'center center',
          transform: {
            xs: 'translate(-50%, -50%) rotate(90deg)',
            md: 'translate(-50%, -50%) rotate(0deg)',
          },
          zIndex: 1,
        }}
      />
      <Box
        sx={{
          position: 'absolute',
          top: '50%',
          left: '50%',
          width: '400px',
          height: '400px',
          backgroundImage: 'url("/img/loginlogo.png")',
          backgroundRepeat: 'no-repeat',
          backgroundPosition: 'center',
          backgroundSize: 'contain',
          transformOrigin: 'center center',
          transform: 'translate(-50%, -50%) ',
          zIndex: 1,
        }}
      />
      <Box
        sx={{
          position: 'relative', // creates stacking context
          zIndex: 10, // higher than 1 for bg and logo
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          height: '100%', // fill parent height for vertical centering
          width: '100%',
        }}>
        {page}
      </Box>
    </Box>
  </Layout>
);

export default SignInPage;
