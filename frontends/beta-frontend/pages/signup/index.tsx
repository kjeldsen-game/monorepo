import { useState } from 'react';
import {
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  Snackbar,
  TextField,
  Typography,
} from '@mui/material';
import { Controller, useForm } from 'react-hook-form';
import { Layout } from '@/shared/layout';
import { NextPageWithLayout } from '@/pages/_app';
import { apiSignIn, apiSignup } from '../api/auth/signup';
import { useRouter } from 'next/navigation';
import MarketButton from '@/shared/components/Market/MarketButton';

export const standardPinkTextFieldSx = {
  '& label': {
    color: '#FF3F84',
  },
  '& label.Mui-focused': {
    color: '#FF3F84',
  },
  '& .MuiInput-underline:before': {
    borderBottom: '2px solid grey',
  },
  '& .MuiInput-underline:hover:before': {
    borderBottom: '2px solid #FF3F84 !important',
  },
  '& .MuiInput-underline:after': {
    borderBottom: '2px solid #FF3F84',
  },
  '& .MuiInputBase-input': {
    color: 'black',
  },
};

interface SignUpFormValues {
  username: string;
  password: string;
  confirmPassword: string;
  teamName: string;
}

const SignUpPage: NextPageWithLayout = () => {
  const [open, setOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const { handleSubmit, control, watch } = useForm<SignUpFormValues>({
    mode: 'onBlur',
    reValidateMode: 'onChange',
  });
  const router = useRouter();

  const doAutoLogin = (username: string, password: string) => {
    apiSignIn(username, password)
      .then(() => {
        router.push('/');
      })
      .catch((error) => {
        console.error(error);
        setErrorMessage('Login with new user failed');
        setOpen(true);
      });
  };

  const handleCloseSnackBar = () => {
    setOpen(false);
  };

  return (
    <Card sx={{ width: { xs: '60%', sm: '60%', md: '40%' }, boxShadow: 24 }} >
      <CardHeader title="Create an account" component={Box} />
      <CardContent>
        <form
          css={{
            display: 'flex',
            flexDirection: 'column',
            alignContent: 'center',
            justifyContent: 'center',
            rowGap: '1rem',
          }}
          onSubmit={handleSubmit(async ({ username, password, teamName }) => {
            apiSignup(username, password, teamName)
              .then(() => {
                console.log('then');
                doAutoLogin(username, password);
              })
              .catch((err) => {
                console.log('catch');
                setErrorMessage(err);
                setOpen(true);
                console.error(err);
              });
          })}>
          <Controller
            name="username"
            control={control}
            defaultValue=""
            rules={{ required: 'Username required' }}
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
              />
            )}
          />
          <Controller
            name="teamName"
            control={control}
            defaultValue=""
            rules={{ required: 'Team Name required' }}
            render={({
              field: { onChange, onBlur, value },
              fieldState: { error },
            }) => (
              <TextField
                sx={standardPinkTextFieldSx}
                label="Team Name"
                variant="standard"
                value={value}
                onChange={onChange}
                onBlur={onBlur}
                error={!!error}
                helperText={error ? error.message : null}
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
          <Controller
            name="confirmPassword"
            control={control}
            defaultValue=""
            rules={{
              required: 'Password required',
              validate: (val: string) => {
                if (watch('password') != val) {
                  return 'Your passwords do no match';
                }
              },
            }}
            render={({
              field: { onChange, onBlur, value },
              fieldState: { error },
            }) => (
              <TextField
                sx={standardPinkTextFieldSx}
                label="Confirm Password"
                variant="standard"
                value={value}
                onChange={onChange}
                onBlur={onBlur}
                type="password"
              />

            )}
          />
          <div style={{ display: 'flex', justifyContent: 'center' }}>
            <MarketButton type='submit' variant="contained">
              Sign Up
            </MarketButton>
            <Typography
              id="errorContainer"
              css={{ color: 'red', marginTop: '1rem' }}></Typography>
          </div>
        </form>
      </CardContent>
      <Snackbar
        open={open}
        autoHideDuration={6000}
        onClose={handleCloseSnackBar}
        message={errorMessage}
      />
    </Card>
  );
};

SignUpPage.getLayout = (page) => (
  <Layout isMenu={false}>
    <Box
      sx={{
        mt: { xs: '-112px', sm: '-128px' },
        position: 'relative',
        width: '100vw',
        height: '100vh',
        overflow: 'hidden',
      }}
    >
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
          backgroundImage:
            'url("/img/loginlogo.png")',
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
          position: 'relative',  // creates stacking context
          zIndex: 10,            // higher than 1 for bg and logo
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          height: '100%',        // fill parent height for vertical centering
          width: '100%',
        }}
      >
        {page}
      </Box>
    </Box>
  </Layout>

);

export default SignUpPage;
