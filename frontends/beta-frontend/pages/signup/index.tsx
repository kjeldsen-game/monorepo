import React, { useState } from 'react'
import { Box, Button, Card, CardContent, CardHeader, Snackbar, TextField, Typography } from '@mui/material'
import { Controller, useForm } from 'react-hook-form'
import { CenterContainer } from '@/shared/layout'
import { NextPageWithLayout } from '@/pages/_app'
import { apiSignIn, apiSignup } from '../api/auth/signup'
import { useRouter } from 'next/navigation'

interface SignUpFormValues {
  username: string
  password: string
  confirmPassword: string
  teamName: string
}

const SignUpPage: NextPageWithLayout = () => {
  const [open, setOpen] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')

  const { handleSubmit, control, watch } = useForm<SignUpFormValues>({
    mode: 'onBlur',
    reValidateMode: 'onChange',
  })
  const router = useRouter()

  const doAutoLogin = (username: string, password: string) => {
    apiSignIn(username, password)
      .then(() => {
        router.push('/')
      })
      .catch((error) => {
        console.error(error)
        setErrorMessage('Login with new user failed')
        setOpen(true)
      })
  }

  const handleCloseSnackBar = () => {
    setOpen(false)
  }

  return (
    <Card>
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
                setTimeout(() => doAutoLogin(username, password), 1000)
              })
              .catch((err) => {
                setErrorMessage(err)
                setOpen(true)
                console.error(err)
              })
          })}>
          <Controller
            name="username"
            control={control}
            defaultValue=""
            rules={{ required: 'Username required' }}
            render={({ field: { onChange, onBlur, value }, fieldState: { error } }) => (
              <TextField
                label="Username"
                variant="filled"
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
            render={({ field: { onChange, onBlur, value }, fieldState: { error } }) => (
              <TextField
                label="Team Name"
                variant="filled"
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
            render={({ field: { onChange, onBlur, value }, fieldState: { error } }) => (
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
          <Controller
            name="confirmPassword"
            control={control}
            defaultValue=""
            rules={{
              required: 'Password required',
              validate: (val: string) => {
                if (watch('password') != val) {
                  return 'Your passwords do no match'
                }
              },
            }}
            render={({ field: { onChange, onBlur, value }, fieldState: { error } }) => (
              <TextField
                label="Confirm Password"
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
              Sign Up
            </Button>
            <Typography id="errorContainer" css={{ color: 'red', marginTop: '1rem' }}></Typography>
          </div>
        </form>
      </CardContent>
      <Snackbar open={open} autoHideDuration={6000} onClose={handleCloseSnackBar} message={errorMessage} />
    </Card>
  )
}

SignUpPage.getLayout = (page) => <CenterContainer>{page}</CenterContainer>

export default SignUpPage
