import React from 'react'
import { Box, Button, Card, CardContent, CardHeader, TextField, Typography } from '@mui/material'
import { Controller, useForm } from 'react-hook-form'
import { CenterContainer } from '@/shared/layout'
import { NextPageWithLayout } from '@/pages/_app'
import { apiSignup } from '../api/auth/signup'

interface SignUpFormValues {
  username: string
  password: string
  confirmPassword: string
  teamName: string
}

const SignUpPage: NextPageWithLayout = () => {
  const { handleSubmit, control, watch } = useForm<SignUpFormValues>({
    mode: 'onBlur',
    reValidateMode: 'onChange',
  })

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
    </Card>
  )
}

SignUpPage.getLayout = (page) => <CenterContainer>{page}</CenterContainer>

export default SignUpPage
